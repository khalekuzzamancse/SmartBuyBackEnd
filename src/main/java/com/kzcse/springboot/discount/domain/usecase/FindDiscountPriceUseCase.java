package com.kzcse.springboot.discount.domain.usecase;

import com.kzcse.springboot.common.ErrorMessage;
import com.kzcse.springboot.discount.data.entity.DiscountByPriceEntity;
import com.kzcse.springboot.discount.data.repository.DiscountByPriceRepository;

public class FindDiscountPriceUseCase {
    private final DiscountByPriceRepository repository;

    public FindDiscountPriceUseCase(DiscountByPriceRepository repository) {
        this.repository = repository;
    }
    public DiscountByPriceEntity findOrThrow(String productId)throws Exception{
        var response=repository.findByProductId(productId);
        if (response.isEmpty()){
            throw new ErrorMessage()
                    .setMessage("Discount with id=" + productId + " does not exits")
                    .setCauses("Not Found in database")
                    .setSource(this.getClass().getSimpleName())
                    .toException();
        }
        return response.get();


    }
}
