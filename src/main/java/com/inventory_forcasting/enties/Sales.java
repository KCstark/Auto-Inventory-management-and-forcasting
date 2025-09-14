package com.inventory_forcasting.enties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sales", indexes = {@Index(name = "idx_saleDate", columnList = "saleDate")})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private LocalDateTime saleDate;//order index: asc
    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "userId")
    private AppUsers user;

    @Override
    public String toString(){
        return "Sales [id=" + id + ", quantity=" + quantity + ", saleDate=" + saleDate + ", product=" + product.getName() + ", user=" + user.getName() + "]";
    }
}
