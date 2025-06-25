package org.sita.repository;

import org.sita.entity.ProductPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ProductPortfolioRepository extends JpaRepository<ProductPortfolio, UUID> {

    @Query("SELECT p.stock FROM ProductPortfolio p WHERE p.productName = :name")
    int findStockByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query("UPDATE ProductPortfolio p SET p.stock = :stock WHERE p.productName = :name")
    void updateStockByName(@Param("name") String name, @Param("stock") int stock);

    @Query("SELECT p.price FROM ProductPortfolio p WHERE p.productName = :name")
    int findPriceByName(@Param("name") String name);
}
