package com.kzcse.springboot.purchase.repositoy;

import com.kzcse.springboot.purchase.entity.PurchasedProductEntity;
import org.springframework.data.repository.CrudRepository;

public interface PurchasedProductRepository extends CrudRepository<PurchasedProductEntity, String> {

}
