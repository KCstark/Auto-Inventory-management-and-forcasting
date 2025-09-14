package com.inventory_forcasting.repo;

import com.inventory_forcasting.enties.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {

    List<Sales> findByProductId(Long productId);
    @Query(value = "SELECT * from sales where product_id= :productId and sale_date between :startDate and :endDate"
            ,nativeQuery = true)
    List<Sales> findByProductIdAndSaleDateBetween(@Param("productId")Long productId,
                                                  @Param("startDate")LocalDateTime startDate,
                                                  @Param("endDate")LocalDateTime endDate);
}
