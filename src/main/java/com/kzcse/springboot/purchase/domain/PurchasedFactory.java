package com.kzcse.springboot.purchase.domain;

import com.kzcse.springboot.purchase.data.repositoy.PurchasedProductRepository;
import com.kzcse.springboot.purchase.domain.usecase.PurchasedHistoryExistenceUseCase;
import org.springframework.stereotype.Component;

@Component
public class PurchasedFactory {
    private final PurchasedProductRepository repository;

    public PurchasedFactory(PurchasedProductRepository repository) {
        this.repository = repository;
    }
    public PurchasedHistoryExistenceUseCase createHistoryUseCase(){
        return  new PurchasedHistoryExistenceUseCase(repository);
    }
}
