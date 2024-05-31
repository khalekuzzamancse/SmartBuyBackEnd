package com.kzcse.springboot.discount.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DiscountByProductRepository extends CrudRepository<DiscountByProductEntity, String> {
    @Query("SELECT d.id FROM DiscountByProductEntity d WHERE d.parentId = ?1 AND ?2 >= d.requiredParentQuantity")
    String findDiscountProductId(String parentId, int purchaseQuantity);
    // Custom JPQL query to retrieve all discounts for a given parentId
    @Query("SELECT d FROM DiscountByProductEntity d WHERE d.parentId = :parentId")
    List<DiscountByProductEntity> findOfferedProduct(String parentId);

}
