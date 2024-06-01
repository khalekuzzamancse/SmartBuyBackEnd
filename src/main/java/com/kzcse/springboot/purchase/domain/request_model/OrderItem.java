package com.kzcse.springboot.purchase.domain.request_model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private String id;
    private String userId;
    private String pid;
    private int quantity;
}