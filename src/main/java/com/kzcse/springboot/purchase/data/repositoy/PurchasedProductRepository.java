package com.kzcse.springboot.purchase.data.repositoy;

import com.kzcse.springboot.purchase.data.entity.PurchasedProductEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PurchasedProductRepository extends CrudRepository<PurchasedProductEntity, String> {
    // JPQL query to find purchased products by userId
    @Query("SELECT p FROM PurchasedProductEntity p WHERE p.userId = ?1")
    List<PurchasedProductEntity> findByUserId(String userId);
}
