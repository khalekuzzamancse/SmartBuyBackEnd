package com.kzcse.springboot.purchase.domain.request_model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class OrderRequest {
    private String userId;
    private String coupon;
    private List<OrderedItem> items;
}
