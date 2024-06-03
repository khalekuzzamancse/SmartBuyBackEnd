package com.kzcse.springboot.purchase.data.service;

import com.kzcse.springboot.purchase.domain.PurchasedFactory;
import com.kzcse.springboot.purchase.domain.request_model.OrderRequest;
import com.kzcse.springboot.purchase.domain.response_model.OrderBillResponse;
import org.springframework.stereotype.Service;

@Service
public class ProductOrderRequestService {

    private final PurchasedFactory purchasedFactory;

    public ProductOrderRequestService(PurchasedFactory purchasedFactory) {
        this.purchasedFactory = purchasedFactory;
    }

    public OrderBillResponse processRequestOrThrow(OrderRequest request) throws Exception {
       return purchasedFactory.createPurchaserBillGenerateUserCase().generateBillOrThrow(request);
    }


}
