package com.kzcse.springboot.return_product.data.repository;

import com.kzcse.springboot.return_product.data.entiry.PendingReturnProduct;
import org.springframework.data.repository.CrudRepository;

public interface ReturnProductRepository extends CrudRepository<PendingReturnProduct, String> {

}
