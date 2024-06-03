package com.kzcse.springboot.inventory.domain.usecase;

import com.kzcse.springboot.common.ErrorMessage;
import com.kzcse.springboot.inventory.data.InventoryEntity;
import com.kzcse.springboot.inventory.data.InventoryRepository;
import com.kzcse.springboot.product.domain.ProductFactory;

public class GetInventoryUseCase {
    private final ProductFactory productFactory;
    private final InventoryRepository repository;

    public GetInventoryUseCase(ProductFactory productFactory, InventoryRepository repository) {
        this.productFactory = productFactory;
        this.repository = repository;
    }

    public InventoryEntity getOrThrow(String productId) throws Exception {
        throwIfProductDoesNotExits(productId);
        return fetchFromDBOrThrow(productId);
    }

    private InventoryEntity fetchFromDBOrThrow(String productId) throws Exception {
        var response = repository.findById(productId);
        if (response.isEmpty()) {
            throw new ErrorMessage()
                    .setMessage("Failed with productId=" + productId)
                    .setCauses("No such element found in database")
                    .setSource(this.getClass().getSimpleName())
                    .toException();
        }
        return response.get();
    }

    private void throwIfProductDoesNotExits(String productId) throws Exception {
        productFactory.createProductAbsentUseCase().throwIfNotExit(productId);
    }

}
