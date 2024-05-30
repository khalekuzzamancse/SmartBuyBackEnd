package com.kzcse.springboot.purchase.model;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReturnRequest {
    private String userId;
    private String productId;
    private int purchasedQuantity;
    private int returnQuantity;
}