package com.kzcse.springboot.purchase.data.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "OrderedTable")
public class OrderItemEntity {
    @Id
    private String id;
    private String userId;
    private String pid;
    private int quantity;
}