package com.kzcse.springboot.purchase.domain.usecase;

import com.kzcse.springboot.common.ErrorMessage;
import com.kzcse.springboot.purchase.data.entity.PurchasedProductEntity;
import com.kzcse.springboot.purchase.data.repositoy.PurchasedProductRepository;

public class SavePurchasedProductUserCase {
    private final PurchasedProductRepository repository;

    public SavePurchasedProductUserCase(PurchasedProductRepository repository) {
        this.repository = repository;
    }

    public void saveOrThrow(PurchasedProductEntity entity) throws Exception {
        var response = repository.save(entity);
        //TODO:Make sure PurchasedProductEntity has override the equal and hashcode
        var isNotSaved = !(response.equals(entity));
        if (isNotSaved) {
            throw new ErrorMessage()
                    .setMessage("Failed; Product with " + entity.getProductId())
                    .setCauses("Failed to save database")
                    .setSource(this.getClass().getSimpleName())
                    .toException();
        }

    }

}
