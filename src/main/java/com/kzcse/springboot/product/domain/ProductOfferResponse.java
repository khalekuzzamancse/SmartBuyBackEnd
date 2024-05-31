package com.kzcse.springboot.product.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Client should not calculate business logic or
 * mathematical calculation
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOfferResponse {
    private String productName;
    private String imageLink;
    private int requiredQuantity;
    private int freeQuantity;
}
