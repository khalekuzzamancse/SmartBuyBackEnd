package com.kzcse.springboot.return_product.domain;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReturningPendingProductModel {
        String purchaseId;
        String freeProductId;
        int purchaseQuantity;//amount that wants to back/return
        int freeItemQuantity; //free product that will be back along with main product
}
