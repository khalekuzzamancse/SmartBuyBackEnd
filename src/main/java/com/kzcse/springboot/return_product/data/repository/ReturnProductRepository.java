package com.kzcse.springboot.return_product.data.repository;

import com.kzcse.springboot.return_product.data.entiry.ReturningPendingEntity;
import org.springframework.data.repository.CrudRepository;

public interface ReturnProductRepository extends CrudRepository<ReturningPendingEntity, String> {

}
