package com.kzcse.springboot.product.domain.model.response_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountByPriceResponse  {
    private int amount;
    private Long expirationTimeInMs;

}
