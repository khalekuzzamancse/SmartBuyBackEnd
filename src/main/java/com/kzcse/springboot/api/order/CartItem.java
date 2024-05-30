package com.kzcse.springboot.api.order;

import com.kzcse.springboot.api.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartItem {
    private int quantity;
    private Product product;
}
