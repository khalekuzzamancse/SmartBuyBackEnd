package com.kzcse.springboot.product.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Client should not calculate business logic or
 * mathematical calculation,it only shows the details,that why returning String
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOfferModel {
    private String productName;
    private String imageLink;
    private String requiredQuantity;
    private String freeQuantity;
}
