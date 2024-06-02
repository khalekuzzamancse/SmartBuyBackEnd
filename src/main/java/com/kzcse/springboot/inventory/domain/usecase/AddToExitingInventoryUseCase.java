package com.kzcse.springboot.inventory.domain.usecase;

import com.kzcse.springboot.common.ErrorMessage;
import com.kzcse.springboot.inventory.data.InventoryRepository;
import com.kzcse.springboot.product.domain.ProductFactory;

public class AddToExitingInventoryUseCase {
    private final ProductFactory productFactory;
    private final InventoryRepository repository;

    public AddToExitingInventoryUseCase(ProductFactory productFactory, InventoryRepository repository) {
        this.productFactory = productFactory;
        this.repository = repository;
    }
    public void addOrThrow(String productId,int amount) throws Exception{
        throwIfProductDoesNotExits(productId);

        var updatedRow=repository.addQuantity(productId,amount);
        var isFailed=!(updatedRow>0);
        if (isFailed){
            throw new ErrorMessage()
                    .setMessage("Failed with productId="+productId+ " with amount="+amount)
                    .setCauses("No Row updated in the database")
                    .setSource(this.getClass().getSimpleName())
                    .toException();
        }

    }
    private void throwIfProductDoesNotExits(String productId) throws  Exception{
        productFactory.createProductAbsentUseCase().throwIfNotExit(productId);
    }

}
