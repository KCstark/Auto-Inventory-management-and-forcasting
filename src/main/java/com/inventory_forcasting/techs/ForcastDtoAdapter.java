package com.inventory_forcasting.techs;

import com.inventory_forcasting.enties.Forecast;
import com.inventory_forcasting.enties.Product;
import com.inventory_forcasting.models.ForecastDTO;
import com.inventory_forcasting.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ForcastDtoAdapter implements DtoEntityAdapter<ForecastDTO, Forecast> {
    private final ProductRepository productRepository;
    @Override
    public Forecast convertToEntity(ForecastDTO dto) {
        Forecast entity= Forecast.builder()
                .forecastDate(dto.getForecastDate())
                .predictedDemand(dto.getPredictedDemand())
                .product(NullCheck.getSafeReference(dto.getProductId(), productRepository::getReferenceById))
                .algorithmUsed(dto.getAlgorithmUsed())
                .build();
        if(dto.getId()!=null){
            entity.setId(dto.getId());
        }
        return entity;
    }

    @Override
    public ForecastDTO convertToDto(Forecast entity) {
        ForecastDTO dto=ForecastDTO.builder()
                .predictedDemand(entity.getPredictedDemand())
                .forecastDate(entity.getForecastDate())
                .algorithmUsed(entity.getAlgorithmUsed())
                .productId(NullCheck.getSafe(entity.getProduct(), Product::getId))
                .build();
        if (entity.getId()!=null){
            dto.setId(entity.getId());
        }
        return dto;
    }
}
