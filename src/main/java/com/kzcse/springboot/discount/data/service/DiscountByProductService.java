package com.kzcse.springboot.discount.data.service;

import com.kzcse.springboot.common.APIResponseDecorator;
import com.kzcse.springboot.discount.data.entity.DiscountByProductEntity;
import com.kzcse.springboot.discount.data.repository.DiscountByProductRepository;
import com.kzcse.springboot.discount.domain.DiscountByProductRequestModel;
import com.kzcse.springboot.product.data.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DiscountByProductService {
    private final DiscountByProductRepository repository;
    private final ProductRepository productRepository;
    public DiscountByProductService(DiscountByProductRepository repository, ProductRepository productRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
    }

    public APIResponseDecorator<String> addDiscount(List<DiscountByProductRequestModel> requestModels) throws Exception {
        checkProductDoesExitOrThrow(requestModels);
            var entities = requestModels.stream().map(this::toModel).toList();
            repository.saveAll(entities);
            return new APIResponseDecorator<String>().onSuccess("Added Successfully");

    }

    private DiscountByProductEntity toModel(DiscountByProductRequestModel model) {
        return new DiscountByProductEntity(
                model.getParentId() + model.getChildId(),
                model.getParentId(),
                model.getChildId(),
                model.getRequiredParentQuantity(),
                model.getFreeChildQuantity(),
                getExpireTimeMs(5)
        );
    }

    private void checkProductDoesExitOrThrow(List<DiscountByProductRequestModel> entities) throws Exception {
        for (DiscountByProductRequestModel requestModel : entities) {
            var doesNotExist = !(productRepository.existsById(requestModel.getParentId()));
            if (doesNotExist) {
                throw new Exception("Product with id " + requestModel.getParentId() +
                        " does not exits\nServerError::DiscountByPriceService::addDiscountByPrice");
            }
             doesNotExist = !(productRepository.existsById(requestModel.getChildId()));
            if (doesNotExist) {
                throw new Exception("Product with id " + requestModel.getChildId() +
                        " does not exits\nServerError::DiscountByPriceService::addDiscountByPrice");
            }
        }

    }


    private long getExpireTimeMs(int daysToAdd) {
        long currentTimeInMs = System.currentTimeMillis();
        long msInADay = TimeUnit.DAYS.toMillis(1);
        long addedTimeInMs = daysToAdd * msInADay;
        return currentTimeInMs + addedTimeInMs;
    }

}
