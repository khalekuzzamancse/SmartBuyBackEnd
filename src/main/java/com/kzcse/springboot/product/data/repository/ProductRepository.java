package com.kzcse.springboot.product.data.repository;

import com.kzcse.springboot.product.data.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<ProductEntity, String> {

}
