package com.kzcse.springboot.purchase.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
public class PurchasedProductEntity {
    @Id
    private String id;
    private String userId;
    private String productId;
    private int quantity;
    private String discountId;
    private LocalDate returnExpireDate;
}