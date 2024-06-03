package com.kzcse.springboot.inventory.domain.usecase;

import com.kzcse.springboot.common.ErrorMessage;
import com.kzcse.springboot.inventory.domain.InventoryFactory;
import com.kzcse.springboot.product.domain.ProductFactory;

public class InventoryValidateUseCase {

    private final ProductFactory productFactory;
    private final InventoryFactory inventoryFactory;

    public InventoryValidateUseCase(ProductFactory productFactory, InventoryFactory inventoryFactory) {
        this.productFactory = productFactory;
        this.inventoryFactory = inventoryFactory;
    }
    public void validateOrThrow(String productId, int desiredPurchaseAmount) throws Exception{
        var entity = inventoryFactory.createGetInventoryUseCase().getOrThrow(productId);
        var availableAmount = entity.getQuantity();
        var productName = productFactory.createProductFinderUseCase().fetchOrThrow(productId).getName();
        var isInventoryExceed = (desiredPurchaseAmount > availableAmount);
        if (isInventoryExceed) {
            throw new ErrorMessage()
                    .setMessage("failed")
                    .setCauses(String.format("The requested quantity for product (name=%s, id=%s) exceeds the available stock of %d", productName, productId, availableAmount))
                    .setSource(this.getClass().getSimpleName())
                    .toException();

        }
        if (desiredPurchaseAmount <= 0) {
            throw new ErrorMessage()
                    .setMessage("failed")
                    .setCauses(String.format("The requested quantity for product (name=%s, id=%s) can not be less than 1", productName, productId))
                    .setSource(this.getClass().getSimpleName())
                    .toException();
        }


    }




}
