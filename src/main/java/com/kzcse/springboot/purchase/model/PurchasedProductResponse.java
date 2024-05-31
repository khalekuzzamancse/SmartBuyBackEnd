package com.kzcse.springboot.purchase.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchasedProductResponse {
    private String purchaseId;
    private String productName;
    private String productImageUrl;
    private int quantity;
    private String returnExpireDate;
}
