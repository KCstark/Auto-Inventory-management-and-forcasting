package com.inventory_forcasting.service;

import com.inventory_forcasting.enties.Product;
import com.inventory_forcasting.enties.Sales;
import com.inventory_forcasting.exceptions.InventoryException;
import com.inventory_forcasting.exceptions.SaleFailedException;
import com.inventory_forcasting.models.EventType;
import com.inventory_forcasting.models.SalesDTO;
import com.inventory_forcasting.repo.AppUserRepo;
import com.inventory_forcasting.repo.ProductRepository;
import com.inventory_forcasting.repo.SalesRepository;
import com.inventory_forcasting.techs.SalesDtoAdapter;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalesService {

    private final SalesRepository salesRepository;
    private final SalesDtoAdapter salesDtoAdapter;
    private final GetEverywhere everywhere;
    private final ProductRepository productRepository;
    private final ForecastService forecastService;
    private final AuditService auditService;


    //      validates product exists, triggers forecast
    //gets product id , quantity from dto
    @Transactional
    public SalesDTO addSalesData(SalesDTO salesDTO) {

        Optional<Product> opProduct = productRepository.findById(salesDTO.getProductId());
        if (opProduct.isPresent() && opProduct.get().getCurrentStock() >= salesDTO.getQuantity()) {
            Product product = opProduct.get();
            product.setCurrentStock(product.getCurrentStock() - salesDTO.getQuantity());
            productRepository.save(product);
        } else if (opProduct.isPresent()) {
            auditService.logEvent(EventType.SALE_RECORDED_FAILED, "POST /sales","Sale recorded unsuccessfully");
            throw new InventoryException("Insufficient stock: " + opProduct.get().getCurrentStock());
        } else {
            auditService.logEvent(EventType.SALE_RECORDED_FAILED, "POST /sales","Sale recorded unsuccessfully");
            throw new InventoryException("Product not found");
        }
        Sales savedSale = null;
        try {
            salesDTO.setUserId(everywhere.userId());
            salesDTO.setSaleDate(LocalDateTime.now());
            savedSale = salesRepository.save(salesDtoAdapter.convertToEntity(salesDTO));
        } catch (Exception e) {
            auditService.logEvent(EventType.SALE_RECORDED_FAILED, "POST /sales","Sale recorded unsuccessfully");
            throw new SaleFailedException("Sale failed for some reason rolling back changes: " + e.getMessage());
        }

        forecastService.notifyForecastEngine(savedSale);
        auditService.logEvent(EventType.SALE_RECORDED, "POST /sales","Sale recorded successfully");
        return salesDtoAdapter.convertToDto(savedSale);
    }

    public Object getAllSalesData() {
        List<Sales> salesList = salesRepository.findAll();
        List<SalesDTO> dtoList = salesList.stream().map(salesDtoAdapter::convertToDto).toList();
        auditService.logEvent(EventType.SALE_RECORDED, "GET /sales","Sale recorded successfully");
        return dtoList;
    }
}
