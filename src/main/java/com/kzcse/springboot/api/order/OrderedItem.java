package com.kzcse.springboot.api.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class OrderedItem {
    private String productId;
    private int quantity;
}
