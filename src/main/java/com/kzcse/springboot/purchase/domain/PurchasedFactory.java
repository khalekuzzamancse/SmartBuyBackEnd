package com.kzcse.springboot.purchase.domain;

import com.kzcse.springboot.discount.domain.DiscountFactory;
import com.kzcse.springboot.product.domain.ProductFactory;
import com.kzcse.springboot.purchase.data.repositoy.PurchasedProductRepository;
import com.kzcse.springboot.purchase.domain.usecase.PurchaseBillGenerateUserCase;
import com.kzcse.springboot.purchase.domain.usecase.PurchaseHistoryUpdaterUseCase;
import com.kzcse.springboot.purchase.domain.usecase.PurchasedHistoryExistenceUseCase;
import com.kzcse.springboot.purchase.domain.usecase.SavePurchasedProductUserCase;
import org.springframework.stereotype.Component;

@Component
public class PurchasedFactory {
    private final PurchasedProductRepository repository;
    private final DiscountFactory discountFactory;
    private final ProductFactory productFactory;



    public PurchasedFactory(PurchasedProductRepository repository, DiscountFactory discountFactory, ProductFactory productFactory) {
        this.repository = repository;

        this.discountFactory = discountFactory;
        this.productFactory = productFactory;
    }

    public PurchasedHistoryExistenceUseCase createHistoryExistenceUseCase() {
        return new PurchasedHistoryExistenceUseCase(repository);
    }

    public SavePurchasedProductUserCase createSaveUseCase() {
        return new SavePurchasedProductUserCase(repository);
    }
    public PurchaseHistoryUpdaterUseCase purchaseHistoryUpdaterUseCase(){return  new PurchaseHistoryUpdaterUseCase(this,repository);}
    public PurchaseBillGenerateUserCase createPurchaserBillGenerateUserCase(){
        return  new PurchaseBillGenerateUserCase(productFactory,discountFactory);
    }
}
