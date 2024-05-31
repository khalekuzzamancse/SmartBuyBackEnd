package com.kzcse.springboot.product.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Client should not calculate business logic,it only shows the details
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ProductDetailsResponse {
    private String productId;
    private String name;
    private List<String> imagesLink;
    private String description;
    private int price;
    private DiscountByPriceResponse discountByPrice;
    private ProductOfferResponse discountByProduct;
    private List<ProductReviewResponse> reviews;
}
