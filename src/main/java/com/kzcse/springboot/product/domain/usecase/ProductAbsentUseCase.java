package com.kzcse.springboot.product.domain.usecase;

//Used by other service that is why centering to avoid duplicate

import com.kzcse.springboot.common.ErrorMessage;
import com.kzcse.springboot.product.data.repository.ProductRepository;


public class ProductAbsentUseCase {
    private final ProductRepository repository;


    public ProductAbsentUseCase(ProductRepository repository) {
        this.repository = repository;
    }

    public void throwIfNotExit(String productId) throws Exception {
        var doesNotExits =!( repository.existsById(productId));
        if (doesNotExits) {
            throw new ErrorMessage()
                    .setMessage("Product with id=" + productId + " does not exits")
                    .setCauses("Not Found in database")
                    .setSource(this.getClass().getSimpleName())
                    .toException();
        }

    }
}
