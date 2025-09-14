package com.inventory_forcasting.enties;

import com.inventory_forcasting.models.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Table(name = "PurchaseOrder", indexes = {
        @Index(name = "idx_orderDate", columnList = "orderDate"),
        @Index(name = "idx_expectedArrivalDate", columnList = "expectedArrivalDate")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
    private LocalDateTime orderDate;//ordered index : asc
    private Double quantityOrdered;
    private LocalDateTime expectedArrivalDate;//order index : asc
    private Status status;

    @Override
    public String toString(){
        return "PurchaseOrder [id=" + id + ", product=" + product.getName() + ", orderDate=" +
                orderDate + ", quantityOrdered=" + quantityOrdered + ", expectedArrivalDate=" +
                expectedArrivalDate + ", status=" + status + "]";
    }
}
