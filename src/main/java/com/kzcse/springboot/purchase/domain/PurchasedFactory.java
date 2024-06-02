package com.kzcse.springboot.purchase.domain;

import com.kzcse.springboot.purchase.data.repositoy.PurchasedProductRepository;
import com.kzcse.springboot.purchase.domain.usecase.PurchaseHistoryUpdaterUseCase;
import com.kzcse.springboot.purchase.domain.usecase.PurchasedHistoryExistenceUseCase;
import com.kzcse.springboot.purchase.domain.usecase.SavePurchasedProductUserCase;
import org.springframework.stereotype.Component;

@Component
public class PurchasedFactory {
    private final PurchasedProductRepository repository;



    public PurchasedFactory(PurchasedProductRepository repository) {
        this.repository = repository;

    }

    public PurchasedHistoryExistenceUseCase createHistoryExistenceUseCase() {
        return new PurchasedHistoryExistenceUseCase(repository);
    }

    public SavePurchasedProductUserCase createSaveUseCase() {
        return new SavePurchasedProductUserCase(repository);
    }
    public PurchaseHistoryUpdaterUseCase purchaseHistoryUpdaterUseCase(){return  new PurchaseHistoryUpdaterUseCase(this,repository);}
}
