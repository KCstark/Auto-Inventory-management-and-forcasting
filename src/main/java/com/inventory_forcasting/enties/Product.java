package com.inventory_forcasting.enties;

import com.inventory_forcasting.models.Categories;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_category", columnList = "category"),
        @Index(name = "idx_leadTimeDays", columnList = "leadTimeDays")
        })
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name cannot be blank")
    private String name;

//    @NotEmpty(message = "Category is required")
    private Categories category;

    @Min(value = 0, message = "Stock cannot be negative")
    private int currentStock;

    @Min(value = 1, message = "Reorder threshold must be at least 1")
    private int reorderThreshold;

    @Min(value = 0, message = "Lead time must be non-negative")
    private int leadTimeDays;//ordered: DESC

    @Positive(message = "Price must be positive")
    private double price;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Sales> sales;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<PurchaseOrder> purchaseOrders;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Forecast> forecasts;

    @Override
    public String toString(){
        return "Product [id=" + id + ", name=" + name + ", category=" + category + ", currentStock=" + currentStock + ", reorderThreshold=" + reorderThreshold + ", leadTimeDays=" + leadTimeDays + ", price=" + price + "]";
    }

}
