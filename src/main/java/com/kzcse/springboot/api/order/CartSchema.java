package com.kzcse.springboot.api.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartSchema {
    private String userId;
    private String productId;
    private int quantity;
    private String id;

    public CartSchema(String userId, String productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.id = userId + productId;
    }
}
