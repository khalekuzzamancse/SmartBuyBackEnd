package com.kzcse.springboot.discount.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "DiscountByProductTable")
public class DiscountByProductEntity {
    @Id
    private String id;
    private String mainProductId;
    private String bonusProductId;
    private int minQuantityForBonus;
    private int bonusQuantity;
    private Long expirationTimeInMs;

}
