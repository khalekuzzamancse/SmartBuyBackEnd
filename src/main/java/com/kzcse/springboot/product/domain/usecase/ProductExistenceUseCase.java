package com.kzcse.springboot.product.domain.usecase;

//Used by other service that is why centering to avoid duplicate

import com.kzcse.springboot.common.ErrorMessage;
import com.kzcse.springboot.product.data.repository.ProductRepository;


public class ProductExistenceUseCase {
    private final ProductRepository repository;


    public ProductExistenceUseCase(ProductRepository repository) {
        this.repository = repository;
    }

    public void throwOnExit(String productId) throws Exception {
        var doesExits = repository.existsById(productId);
        if (doesExits) {
            throw new ErrorMessage()
                    .setMessage("Product with id=" + productId + " already exits")
                    .setCauses("Found in database")
                    .setSource(this.getClass().getSimpleName())
                    .toException();
        }

    }
}
