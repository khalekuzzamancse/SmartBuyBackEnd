package com.kzcse.springboot.discount.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DiscountByProductRequestModel {
    private String mainProductId;
    private String bonusProductId;
    private int minQuantityForBonus;
    private int bonusQuantity;
    private Long expirationTimeInMs;

}
