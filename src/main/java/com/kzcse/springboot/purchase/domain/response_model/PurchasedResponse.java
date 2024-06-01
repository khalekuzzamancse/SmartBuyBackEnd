package com.kzcse.springboot.purchase.domain.response_model;

import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PurchasedResponse {
    @Id
    private String purchaseId;
    private String discountId;
    //because Local data may not be serializable to client
    private String returnExpireDate;
}