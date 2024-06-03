package com.kzcse.springboot.discount.data.repository;

import com.kzcse.springboot.discount.data.entity.DiscountByPriceEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DiscountByPriceRepository extends CrudRepository<DiscountByPriceEntity, String> {

    Optional<DiscountByPriceEntity> findByProductId(String productId);
}
