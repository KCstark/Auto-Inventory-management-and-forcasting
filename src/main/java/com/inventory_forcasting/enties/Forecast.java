package com.inventory_forcasting.enties;

import com.inventory_forcasting.models.Algorithm;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Forecast", indexes = {
        @Index(name = "idx_algorithmUsed", columnList = "algorithmUsed"),
        @Index(name = "idx_forecastDate", columnList = "forecastDate")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Forecast {
    //(id, productId, forecastDate, predictedDemand, algorithmUsed)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;
    private LocalDateTime forecastDate;//ordered index : asc
    private Double predictedDemand;
    private Algorithm algorithmUsed;

    @Override
    public String toString(){
        return "Forecast [id=" + id + ", product=" + product.getName() + ", forecastDate=" + forecastDate + ", predictedDemand=" + predictedDemand + ", algorithmUsed=" + algorithmUsed + "]";
    }
}
