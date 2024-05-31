package com.kzcse.springboot.discount.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

/**
 * - Expire time in ms so that can represent exact time and data
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountByPriceModel {
    private String productId;
    private int amount;
    private Long expirationTimeInMs;

}
