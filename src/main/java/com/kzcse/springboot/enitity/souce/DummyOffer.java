package com.kzcse.springboot.enitity.souce;

import com.kzcse.springboot.discount.data.DiscountByProductEntity;

import java.util.List;

public class DummyOffer {
    public List<DiscountByProductEntity> offers = List.of(
            new DiscountByProductEntity("1", "1", "10", 2, 1),
            new DiscountByProductEntity("2", "2", "6", 3, 1),
            new DiscountByProductEntity("3", "3", "7", 1, 1),
            new DiscountByProductEntity("4", "4", "8", 5, 2),
            new DiscountByProductEntity("5", "5", "9", 10, 3)
    );
}
