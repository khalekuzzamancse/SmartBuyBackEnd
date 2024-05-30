package com.kzcse.springboot.enitity.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductEntity {
    // Getter and Setter methods
    @Id
    private String pid;
    private String name;
    private int price;
    private String imageLink;
    private String description;

}
