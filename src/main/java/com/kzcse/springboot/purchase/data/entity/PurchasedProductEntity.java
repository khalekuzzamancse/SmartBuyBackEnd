package com.kzcse.springboot.purchase.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "PurchasedProductTable")
public class PurchasedProductEntity {
    @Id
    private String id;
    private String userId;
    private String productId;
    private int quantity;
    private String discountId;
    private LocalDate returnExpireDate;
}