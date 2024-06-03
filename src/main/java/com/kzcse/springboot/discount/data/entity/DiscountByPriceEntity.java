package com.kzcse.springboot.discount.data.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "discount_by_price_table") //otherwise causes error to create database
public class DiscountByPriceEntity {
    @Id
    private String productId;
    private int amount;
    private Long expirationTimeInMs;

}
