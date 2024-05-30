package com.kzcse.springboot.api.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {
    private int totalPrice;
    private String coupon;
    private int discount;
    private String requestId;
    public  OrderResponse(int totalPrice){
        this.totalPrice=totalPrice;
    }
}
