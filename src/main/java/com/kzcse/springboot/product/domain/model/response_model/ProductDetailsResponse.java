package com.kzcse.springboot.product.domain.model.response_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


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
