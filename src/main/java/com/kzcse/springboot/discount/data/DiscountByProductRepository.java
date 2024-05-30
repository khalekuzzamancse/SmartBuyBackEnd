package com.kzcse.springboot.discount.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DiscountByProductRepository extends CrudRepository<DiscountByProductEntity, String> {
    @Query("SELECT d.id FROM DiscountByProductEntity d WHERE d.parentId = ?1 AND ?2 >= d.requiredParentQuantity")
    String findDiscountId(String parentId, int purchaseQuantity);

}
