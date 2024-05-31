package com.kzcse.springboot.purchase.model;

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
        this.requestId="randomid";//used so that client does not give a null key
    }
}
