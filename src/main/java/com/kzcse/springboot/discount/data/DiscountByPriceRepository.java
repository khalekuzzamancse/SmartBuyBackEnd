package com.kzcse.springboot.discount.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DiscountByPriceRepository extends CrudRepository<DiscountByPriceEntity, String> {


}
