package com.kzcse.springboot.return_product.domain;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReturnRequest {
    private String purchaseId;
    private int returnQuantity;
}