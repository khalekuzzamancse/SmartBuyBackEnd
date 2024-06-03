package com.kzcse.springboot.purchase.data.service;

import com.kzcse.springboot.purchase.domain.PurchasedFactory;
import com.kzcse.springboot.purchase.domain.request_model.OrderRequest;
import org.springframework.stereotype.Service;

@Service
public class ProductOrderRequestService {

    private final PurchasedFactory purchasedFactory;

    public ProductOrderRequestService(PurchasedFactory purchasedFactory) {
        this.purchasedFactory = purchasedFactory;
    }

    public String processRequestOrThrow(OrderRequest request) throws Exception {
        var total = purchasedFactory.createPurchaserBillGenerateUserCase().generateBillOrThrow(request);
        return String.format("Have to pay:%d", total);
    }


}
