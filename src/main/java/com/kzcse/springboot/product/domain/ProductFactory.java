package com.kzcse.springboot.product.domain;

import com.kzcse.springboot.product.data.repository.ProductRepository;
import com.kzcse.springboot.product.domain.usecase.ProductAbsentUseCase;
import com.kzcse.springboot.product.domain.usecase.ProductExistenceUseCase;
import com.kzcse.springboot.product.domain.usecase.ProductFinderUseCase;
import org.springframework.stereotype.Component;

@Component
public class ProductFactory {
    private final ProductRepository repository;

    public ProductFactory(ProductRepository repository) {
        this.repository = repository;
    }

    public ProductExistenceUseCase createProductExistenceUseCase() {
        return new ProductExistenceUseCase(repository);
    }

    public ProductAbsentUseCase createProductAbsentUseCase() {
        return new ProductAbsentUseCase(repository);
    }

    public ProductFinderUseCase createProductFinderUseCase() {
        return new ProductFinderUseCase(repository);
    }
}
