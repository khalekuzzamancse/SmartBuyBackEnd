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
    private int bonusEligibilityThreshold;
    private int bonusOnThreshold;
    private Long expirationTimeInMs;

}
