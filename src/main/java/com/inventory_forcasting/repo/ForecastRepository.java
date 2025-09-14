package com.inventory_forcasting.repo;

import com.inventory_forcasting.enties.Forecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    @Query(value = "select * from forecast where forecast_date between :start and :end", nativeQuery = true)
//    @Query("SELECT f FROM Forecast f WHERE f.forecastDate BETWEEN :start AND :end")
    List<Forecast> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "select * from forecast where product_id= :id order by forecast_date desc limit 1",nativeQuery = true)
    Forecast findLatestForecastByProductId(@Param("id") Long id);
}
