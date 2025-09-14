package com.inventory_forcasting.service;

import com.inventory_forcasting.enties.Forecast;
import com.inventory_forcasting.enties.Product;
import com.inventory_forcasting.exceptions.ProductValidationException;
import com.inventory_forcasting.exceptions.ResourceNotFoundException;
import com.inventory_forcasting.models.EventType;
import com.inventory_forcasting.models.ProductDTO;
import com.inventory_forcasting.repo.ForecastRepository;
import com.inventory_forcasting.repo.ProductRepository;
import com.inventory_forcasting.techs.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDtoAdapter productDtoAdapter;
    private final AuditService auditService;
    private final ForecastRepository forecastRepository;
    private final ForcastDtoAdapter forecastDtoAdapter;

    List<ProductValidationStrategy> strategies;

    public ProductService(ProductRepository productRepository,ForcastDtoAdapter forecastDtoAdapter ,ForecastRepository forecastRepository, ProductDtoAdapter productDtoAdapter, AuditService auditService) {
        this.productRepository = productRepository;
        this.productDtoAdapter = productDtoAdapter;
        this.auditService = auditService;
        this.forecastRepository = forecastRepository;
        this.forecastDtoAdapter = forecastDtoAdapter;

        strategies = List.of(
                new CategoryValidation(),
                new ThresholdValidation(),
                new LeadTimeValidation(),
                new PriceValidation(),
                new NameValidation());
    }

    private void validateProduct(Product product) {
        for (ProductValidationStrategy strategy : strategies) {
            if (!strategy.validate(product)) {
                throw new ProductValidationException("Product validation Failed: " + strategy.getMessage());
            }
        }
    }

    public ProductDTO addProduct(ProductDTO product) {
        Product productEntity = productDtoAdapter.convertToEntity(product);
        validateProduct(productEntity);
        productEntity = productRepository.save(productEntity);
        auditService.logEvent(EventType.PRODUCT_CREATED, "POST /products", "Product created successfully");
        return productDtoAdapter.convertToDto(productEntity);
    }


    public ProductDTO getProductData(long id) {
        Product productEntity = productRepository.findById(id)
                .orElseThrow(() ->
                {
                    auditService.logEvent(EventType.PRODUCT_GET_FAILED, "GET /products", "Product retrieved unsuccessfully");
                    return new ResourceNotFoundException("Product not found id: " + id);
                });
        Forecast forecast = forecastRepository.findLatestForecastByProductId(productEntity.getId());
        auditService.logEvent(EventType.PRODUCT_GET, "GET /products/{id}", "Product retrieved successfully");
        ProductDTO dto = productDtoAdapter.convertToDto(productEntity);
        dto.setForecast(forecastDtoAdapter.convertToDto(forecast));
        return dto;
    }

    public List<ProductDTO> getAllProducts() {
        auditService.logEvent(EventType.PRODUCT_GET_ALL, "GET /products", "Product retrieved successfully");
        return productRepository.findAll().stream()
                .map(productDtoAdapter::convertToDto)
                .toList();
    }
}
