package com.kzcse.springboot.discount.domain;

import lombok.*;

/**
 * - Expire time in ms so that can represent exact time and data
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountByPriceRequestModel {
    private String productId;
    private int amount;
    private Long expirationTimeInMs;

}
