package com.kzcse.springboot.discount.data.repository;

import com.kzcse.springboot.discount.data.entity.DiscountByProductEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DiscountByProductRepository extends CrudRepository<DiscountByProductEntity, String> {

    /**
     * <ul>
     *   <li>It is possible that somehow a parentId is present multiple time,in different row,that is why returning list,otherwise causes exception</li>
     *   <li>Details contain product basic info,offer by price,offer by product,reviews</li>
     * </ul>
     */
    @Query("SELECT d.id FROM DiscountByProductEntity d WHERE d.mainProductId = ?1 AND ?2 >= d.bonusEligibilityThreshold")
    List<String> findDiscountProductId(String parentId, int purchaseQuantity);

    // Custom JPQL query to retrieve all discounts for a given parentId
    @Query("SELECT d FROM DiscountByProductEntity d WHERE d.mainProductId = :parentId")
    List<DiscountByProductEntity> findOfferedProduct(String parentId);

}
