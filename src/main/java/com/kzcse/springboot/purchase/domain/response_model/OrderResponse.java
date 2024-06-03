package com.kzcse.springboot.purchase.domain.response_model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class OrderResponse {
    private String productId;
    private String productName;
    private int unitPrice;
    private int quantity;
    private int discount;
    private int originalPrice;
    private int discountedPrice;

}
