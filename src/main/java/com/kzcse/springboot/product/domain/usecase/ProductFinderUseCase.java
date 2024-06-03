package com.kzcse.springboot.product.domain.usecase;

import com.kzcse.springboot.common.ErrorMessage;
import com.kzcse.springboot.product.data.entity.ProductEntity;
import com.kzcse.springboot.product.data.repository.ProductRepository;

public class ProductFinderUseCase {
    private final ProductRepository repository;

    public ProductFinderUseCase(ProductRepository repository) {
        this.repository = repository;
    }
    public ProductEntity fetchOrThrow(String  productId) throws  Exception{
       var response= repository.findById(productId);
       if (response.isEmpty()){
           throw new ErrorMessage()
                   .setMessage("Failed with productId=" + productId)
                   .setCauses("No such element found in database")
                   .setSource(this.getClass().getSimpleName())
                   .toException();
       }
       return response.get();
    }
}
