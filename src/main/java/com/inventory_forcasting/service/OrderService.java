package com.inventory_forcasting.service;

import com.inventory_forcasting.enties.Forecast;
import com.inventory_forcasting.enties.Product;
import com.inventory_forcasting.enties.PurchaseOrder;
import com.inventory_forcasting.exceptions.ResourceNotFoundException;
import com.inventory_forcasting.models.EventType;
import com.inventory_forcasting.models.PurchaseOrderDTO;
import com.inventory_forcasting.models.Status;
import com.inventory_forcasting.repo.ForecastRepository;
import com.inventory_forcasting.repo.ProductRepository;
import com.inventory_forcasting.repo.PurchaseOrderRepository;
import com.inventory_forcasting.techs.PurchaseOrderDtoAdapter;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final PurchaseOrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ForecastRepository forecastRepository;
    private final PurchaseOrderDtoAdapter purchaseOrderDtoAdapter;
    private final AuditService auditService;

    public String autoGenerateOrders() {

        //find all the product whose curr_stock is less than reorderThreshold
        //use latest forecast to determine order quantity
        //calculate expectedArrivalDate using leadTimeDays
        //set status as PENDING
        List<Product> products = productRepository.findrestockableproducts();
        Thread t = new Thread(() -> {
            try {
                generateOrders(products);
            } catch (Exception e) {
                auditService.logEvent(EventType.ORDER_GENERATED_ERROR, "POST /orders/auto", "Orders generated unsuccessfully");
                throw new RuntimeException("Error while saving orders : "+e.getMessage());
            }
        });
        t.start();
        auditService.logEvent(EventType.ORDER_GENERATING, "POST /orders/auto", "Orders generation in progress");
        return "Generating orders asynchronously for these products: "+
                products.stream()
                        .map(Product::getName)
                        .toList();
    }

    private void generateOrders(List<Product> products) throws Exception{
        List<Forecast> forecastList = new ArrayList<>();
        for(Product product : products){//product(0)-> forcast(0)
            Forecast forecast = forecastRepository.findLatestForecastByProductId(product.getId());
            forecastList.add(forecast);
        }

        List<PurchaseOrderDTO> purchaseOrderList = new ArrayList<>();
        for(int i=0;i<products.size();i++){
            Product product = products.get(i);
            Forecast forecast = forecastList.get(i);
            PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                    .product(product)
                    .quantityOrdered(Math.abs(product.getCurrentStock()-forecast.getPredictedDemand()+1))
                    .orderDate(LocalDateTime.now())
                    .expectedArrivalDate(LocalDateTime.now().plusDays(product.getLeadTimeDays()))
                    .status(Status.PENDING)
                    .build();
            purchaseOrderList.add(purchaseOrderDtoAdapter.convertToDto(orderRepository.save(purchaseOrder)));
        }
        auditService.logEvent(EventType.ORDER_GENERATED, "POST /orders/auto", "Orders generated successfully: "+purchaseOrderList);
        log.info("Auto-generated orders: {}", purchaseOrderList);
    }

//mark order received & update stock
    public PurchaseOrderDTO receiveOrder(Long id) {
        PurchaseOrder purchaseOrder = orderRepository.findById(id)
                .orElseThrow(() -> {
                    auditService.logEvent(EventType.ORDER_RECEIVED_ERROR, "POST /orders/{id}/receive", "Order not found id : " + id);
                    return new ResourceNotFoundException("Order not found id : " + id);
                });
        purchaseOrder.setStatus(Status.RECEIVED);
        purchaseOrder.getProduct().setCurrentStock((int) (purchaseOrder.getProduct().getCurrentStock()+purchaseOrder.getQuantityOrdered()));
        productRepository.save(purchaseOrder.getProduct());

        auditService.logEvent(EventType.ORDER_RECEIVED, "POST /orders/{id}/receive", "Order received successfully");

        return purchaseOrderDtoAdapter.convertToDto(orderRepository.save(purchaseOrder));
    }

    public List<PurchaseOrderDTO> getAllOrders() {
        auditService.logEvent(EventType.ORDER_GET_ALL, "GET /orders", "Orders retrieved successfully");
        return orderRepository.findAll().stream()
                .map(purchaseOrderDtoAdapter::convertToDto)
                .toList();
    }
}
