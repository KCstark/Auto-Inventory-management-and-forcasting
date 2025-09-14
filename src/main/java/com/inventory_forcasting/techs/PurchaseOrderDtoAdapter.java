package com.inventory_forcasting.techs;

import com.inventory_forcasting.enties.Product;
import com.inventory_forcasting.enties.PurchaseOrder;
import com.inventory_forcasting.models.PurchaseOrderDTO;
import com.inventory_forcasting.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PurchaseOrderDtoAdapter implements DtoEntityAdapter<PurchaseOrderDTO, PurchaseOrder> {

    private final ProductRepository productRepository;

    @Override
    public PurchaseOrder convertToEntity(PurchaseOrderDTO dto) {
        PurchaseOrder entity = PurchaseOrder.builder()
                .orderDate(dto.getOrderDate())
                .expectedArrivalDate(dto.getExpectedArrivalDate())
                .product(NullCheck.getSafeReference(dto.getProductId(), p -> productRepository.getReferenceById(p)))
                .quantityOrdered(dto.getQuantityOrdered())
                .status(dto.getStatus())
                .build();
        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        return entity;
    }

    @Override
    public PurchaseOrderDTO convertToDto(PurchaseOrder entity) {
        PurchaseOrderDTO dto = PurchaseOrderDTO.builder()
                .orderDate(entity.getOrderDate())
                .expectedArrivalDate(entity.getExpectedArrivalDate())
                .quantityOrdered(entity.getQuantityOrdered())
                .status(entity.getStatus())
                .productId(NullCheck.getSafe(entity.getProduct(), Product::getId))
                .build();
        if (entity.getId() != null) {
            dto.setId(entity.getId());
        }
        return dto;
    }
}
