package com.kzcse.springboot.purchase.domain.usecase;

import com.kzcse.springboot.common.ErrorMessage;
import com.kzcse.springboot.purchase.data.entity.PurchasedProductEntity;
import com.kzcse.springboot.purchase.data.repositoy.PurchasedProductRepository;

public class PurchasedHistoryExistenceUseCase {
    private final PurchasedProductRepository purchasedProductRepository;

    public PurchasedHistoryExistenceUseCase(PurchasedProductRepository purchasedProductRepository) {
        this.purchasedProductRepository = purchasedProductRepository;
    }

    public PurchasedProductEntity throwIfDoesNotExits(String purchasedId) throws Exception {
        var response = purchasedProductRepository.findById(purchasedId);
        if (response.isEmpty()) {
            throw new ErrorMessage()
                    .setMessage("Purchase history  with id=" + purchasedId + " does not exits")
                    .setCauses("Not Found in database")
                    .setSource(this.getClass().getSimpleName())
                    .toException();
        }
        return response.get();
    }
}
