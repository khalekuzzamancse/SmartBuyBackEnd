package com.kzcse.springboot.return_product.domain;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReturningPendingProductModel {
        String purchaseId;
        String bonusProductId;
        int mainReturnQuantity;//amount that wants to back/return
        int bonusReturnQuantity; //free product that will be back along with main product
}
