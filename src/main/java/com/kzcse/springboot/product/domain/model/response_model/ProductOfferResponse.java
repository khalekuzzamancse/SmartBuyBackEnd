package com.kzcse.springboot.product.domain.model.response_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOfferResponse {
    private String productName;
    private String imageLink;
    private int requiredQuantity;
    private int freeQuantity;
    private Long expirationTimeInMs;
}
