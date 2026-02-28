package com.car.management.cars.car;

import com.car.management.cars.car_search.BrandCount;
import com.car.management.cars.car_search.ModelCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Long>, JpaSpecificationExecutor<CarEntity> {

    List<CarEntity> findAllByOwnerOrderByCreationDate(String owner);

    @Query(value = "SELECT brand AS name, COUNT(*) AS total FROM car_entity GROUP BY brand", nativeQuery = true)
    List<BrandCount> countCarsByBrand();

    @Query(value = "SELECT model AS name, COUNT(*) AS total FROM car_entity WHERE brand = :brand GROUP BY model",
            nativeQuery = true)
    List<ModelCount> countCarsByModel(@Param("brand") String brand);

    @Query(value = "SELECT COUNT(*) FROM car_entity WHERE brand = :brand AND model = :model " +
            "AND production_year >= :minYear AND production_year <= :maxYear",
            nativeQuery = true)
    Integer extractFoundByNameNumber(@Param("brand") String brand, @Param("model") String model,
                                     @Param("minYear") Integer minYear, @Param("maxYear") Integer maxYear);
}
