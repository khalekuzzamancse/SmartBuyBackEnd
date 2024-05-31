package com.kzcse.springboot.discount.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DiscountByProductRequestModel {
    private String parentId;
    private String childId;
    private int requiredParentQuantity;
    private int freeChildQuantity;
    private Long expirationTimeInMs;

}
