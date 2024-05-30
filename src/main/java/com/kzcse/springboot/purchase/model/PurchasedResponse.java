package com.kzcse.springboot.purchase.model;

import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PurchasedResponse {
    @Id
    private String purchaseId;
    private String discountId;
    private LocalDate returnExpireDate;
}