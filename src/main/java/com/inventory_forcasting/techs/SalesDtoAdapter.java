package com.inventory_forcasting.techs;

import com.inventory_forcasting.enties.AppUsers;
import com.inventory_forcasting.enties.Sales;
import com.inventory_forcasting.models.SalesDTO;
import com.inventory_forcasting.repo.AppUserRepo;
import com.inventory_forcasting.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SalesDtoAdapter implements DtoEntityAdapter<SalesDTO, Sales>{

    private final ProductRepository productRepository;
    private final AppUserRepo appUserRepo;
    @Override
    public Sales convertToEntity(SalesDTO dto) {
        Sales sales = Sales.builder()
                .quantity(dto.getQuantity())
                .saleDate(dto.getSaleDate())
                .product(NullCheck.getSafeReference(dto.getProductId(),productRepository::getReferenceById))
                .user(NullCheck.getSafeReference(dto.getUserId(),appUserRepo::getReferenceById))
                .build();
        if(dto.getId()!=null){
            sales.setId(dto.getId());
        }
        return sales;
    }

    @Override
    public SalesDTO convertToDto(Sales entity) {
        SalesDTO dto = SalesDTO.builder()
                .quantity(entity.getQuantity())
                .saleDate(entity.getSaleDate())
                .productId(entity.getProduct().getId())
                .userId(NullCheck.getSafe(entity.getUser(), AppUsers::getId))
                .build();
        if(entity.getId()!=null){
            dto.setId(entity.getId());
        }
        return dto;
    }
}
