package com.kzcse.springboot.purchase.domain.usecase;

import com.kzcse.springboot.common.ErrorMessage;
import com.kzcse.springboot.purchase.data.entity.PurchasedProductEntity;
import com.kzcse.springboot.purchase.data.repositoy.PurchasedProductRepository;
import com.kzcse.springboot.purchase.domain.PurchasedFactory;

public class PurchaseHistoryUpdaterUseCase {
    private final PurchasedFactory purchasedFactory;
    private final PurchasedProductRepository purchasedProductRepository;

    public PurchaseHistoryUpdaterUseCase(PurchasedFactory purchasedFactory, PurchasedProductRepository purchasedProductRepository) {
        this.purchasedFactory = purchasedFactory;
        this.purchasedProductRepository = purchasedProductRepository;
    }


    public void updateOrThrow(String purchasedId, int newQuantity, boolean removeDiscount) throws Exception {
        throwIfNotExits(purchasedId);
        throwOnInvalidAmount(newQuantity);

        var entity = getPurchaseEntityOrThrow(purchasedId);
        if (newQuantity == 0) {
            purchasedProductRepository.delete(entity);
            return;
        }
        entity.setQuantity(newQuantity);
        if (removeDiscount) {
            entity.setDiscountId(null);
        }


        saveOrThrow(entity);


    }

    private PurchasedProductEntity getPurchaseEntityOrThrow(String purchaseId) throws Exception {
        return purchasedFactory.createHistoryExistenceUseCase().getOrThrow(purchaseId);
    }

    private void throwIfNotExits(String purchaseId) throws Exception {
        purchasedFactory.createHistoryExistenceUseCase().getOrThrow(purchaseId);
    }

    private void saveOrThrow(PurchasedProductEntity entity) throws Exception {
        purchasedFactory.createSaveUseCase().saveOrThrow(entity);
    }

    private void throwOnInvalidAmount(int amount) throws Exception {
        if (amount < 0) {
            throw new ErrorMessage()
                    .setMessage("Failed to update PurchasedProduct table")
                    .setCauses("Amount is Invalid(negative)")
                    .setSource(this.getClass().getSimpleName())
                    .toException();
        }

    }
}
