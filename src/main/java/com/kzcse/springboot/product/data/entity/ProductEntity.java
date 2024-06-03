package com.kzcse.springboot.product.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ProductTable")
public class ProductEntity {
    // Getter and Setter methods
    @Id
    private String pid;
    private String name;
    private int price;
    private String imageLink;
    private String description;

}
