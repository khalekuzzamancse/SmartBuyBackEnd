package com.kzcse.springboot.discount.domain;

import com.kzcse.springboot.discount.data.repository.DiscountByPriceRepository;
import com.kzcse.springboot.discount.domain.usecase.FindDiscountPriceUseCase;
import org.springframework.stereotype.Component;

@Component
public class DiscountFactory {
    private final DiscountByPriceRepository discountByPriceRepository;

    public DiscountFactory(DiscountByPriceRepository discountByPriceRepository) {
        this.discountByPriceRepository = discountByPriceRepository;
    }
    public FindDiscountPriceUseCase createFindDiscountByPriceUserCase(){
        return  new FindDiscountPriceUseCase(discountByPriceRepository);
    }
}
