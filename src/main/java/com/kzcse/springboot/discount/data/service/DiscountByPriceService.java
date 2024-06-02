package com.kzcse.springboot.discount.data.service;

import com.kzcse.springboot.common.APIResponseDecorator;
import com.kzcse.springboot.common.ErrorMessage;
import com.kzcse.springboot.discount.data.entity.DiscountByPriceEntity;
import com.kzcse.springboot.discount.data.repository.DiscountByPriceRepository;
import com.kzcse.springboot.discount.domain.DiscountByPriceRequestModel;
import com.kzcse.springboot.product.data.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountByPriceService {
    private final DiscountByPriceRepository discountByPriceRepository;
    private final ProductRepository productRepository;

    public DiscountByPriceService(DiscountByPriceRepository discountByPriceRepository, ProductRepository productRepository) {
        this.discountByPriceRepository = discountByPriceRepository;
        this.productRepository = productRepository;
    }

    public void addDiscountByPriceOrThrow(List<DiscountByPriceRequestModel> entities) throws Exception {
        checkProductDoesExitOrThrow(entities);
        discountByPriceRepository.saveAll(entities.stream().map(this::toModel).toList());
    }


    private void checkProductDoesExitOrThrow(List<DiscountByPriceRequestModel> entities) throws Exception {
        for (DiscountByPriceRequestModel requestModel : entities) {
            var productId = requestModel.getProductId();
            if (!productRepository.existsById(productId)) {
                throw new ErrorMessage()
                        .setMessage("failed to add addDiscountByPrice")
                        .setCauses(productId + "does not exits in  database")
                        .setSource("DiscountByPriceService::addDiscountByPriceOrThrow::checkProductDoesExitOrThrow")
                        .toException();
            }
        }

    }

    private DiscountByPriceEntity toModel(DiscountByPriceRequestModel model) {
        var entity = new DiscountByPriceEntity();
        entity.setProductId(model.getProductId());
        entity.setExpirationTimeInMs(model.getExpirationTimeInMs());
        entity.setAmount(model.getAmount());
        return entity;
    }
}
