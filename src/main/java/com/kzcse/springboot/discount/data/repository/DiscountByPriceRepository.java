package com.kzcse.springboot.discount.data.repository;

import com.kzcse.springboot.discount.data.entity.DiscountByPriceEntity;
import org.springframework.data.repository.CrudRepository;

public interface DiscountByPriceRepository extends CrudRepository<DiscountByPriceEntity, String> {


}
