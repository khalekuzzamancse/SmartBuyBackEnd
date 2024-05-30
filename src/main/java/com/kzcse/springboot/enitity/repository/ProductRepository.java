package com.kzcse.springboot.enitity.repository;

import com.kzcse.springboot.enitity.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<ProductEntity, String> {

}
