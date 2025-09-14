package com.inventory_forcasting.techs;

import com.inventory_forcasting.enties.Product;
import com.inventory_forcasting.models.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductDtoAdapter implements DtoEntityAdapter<ProductDTO, Product>{
    @Override
    public Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setPrice(dto.getPrice());
        product.setLeadTimeDays(dto.getLeadTimeDays());
        product.setReorderThreshold(dto.getReorderThreshold());
        product.setCurrentStock(dto.getCurrentStock());
        if(dto.getId()!=null){
            product.setId(dto.getId());
        }
        return product;
    }

    @Override
    public ProductDTO convertToDto(Product entity) {
        ProductDTO productDTO=ProductDTO.builder()
                .name(entity.getName())
                .category(entity.getCategory())
                .currentStock(entity.getCurrentStock())
                .reorderThreshold(entity.getReorderThreshold())
                .leadTimeDays(entity.getLeadTimeDays())
                .price(entity.getPrice())
                .build();
        if(entity.getId()!=null){
            productDTO.setId(entity.getId());
        }

        return productDTO;
    }
}
