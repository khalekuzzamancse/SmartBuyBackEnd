package com.kzcse.springboot.purchase.model;
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