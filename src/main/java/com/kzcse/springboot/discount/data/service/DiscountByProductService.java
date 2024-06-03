package com.kzcse.springboot.discount.data.service;

import com.kzcse.springboot.common.ErrorMessage;
import com.kzcse.springboot.discount.data.entity.DiscountByProductEntity;
import com.kzcse.springboot.discount.data.repository.DiscountByProductRepository;
import com.kzcse.springboot.discount.domain.DiscountByProductRequestModel;
import com.kzcse.springboot.product.data.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

@Service
public class DiscountByProductService {
    private final DiscountByProductRepository repository;
    private final ProductRepository productRepository;

    public DiscountByProductService(DiscountByProductRepository repository, ProductRepository productRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
    }


    public void addDiscountOrThrow(List<DiscountByProductRequestModel> requestModels) throws Exception {
        checkProductDoesExitOrThrow(requestModels);

        var entities = requestModels.stream().map(this::toEntity).toList();
        //TODO:check saved or not
        repository.saveAll(entities);
    }

    public List<DiscountByProductEntity> getAllDiscount() throws Exception {
        return StreamSupport
                .stream(repository
                        .findAll()
                        .spliterator(), false
                )
                .toList();
    }


    private DiscountByProductEntity toEntity(DiscountByProductRequestModel model) {
        var id = model.getMainProductId() +"-"+ model.getBonusProductId()+"-"+model.getBonusEligibilityThreshold()+"-"+model.getBonusOnThreshold();
        return new DiscountByProductEntity(
                id,
                model.getMainProductId(),
                model.getBonusProductId(),
                model.getBonusEligibilityThreshold(),
                model.getBonusOnThreshold(),
                getExpireTimeMs(5)
        );
    }


    private void checkProductDoesExitOrThrow(List<DiscountByProductRequestModel> entities) throws Exception {
        for (DiscountByProductRequestModel requestModel : entities) {
            var parentId = requestModel.getMainProductId();
            var doesNotExist = !(productRepository.existsById(parentId));
            if (doesNotExist) {
                throw new ErrorMessage()
                        .setMessage("failed to add addDiscountByPrice")
                        .setCauses(parentId + "does not exits in  database")
                        .setSource("DiscountByProductService::checkProductDoesExitOrThrow")
                        .toException();
            }
            var childId = requestModel.getBonusProductId();
            doesNotExist = !(productRepository.existsById(childId));
            if (doesNotExist) {
                throw new ErrorMessage()
                        .setMessage("failed to add addDiscountByPrice")
                        .setCauses(childId + "does not exits in  database")
                        .setSource("DiscountByProductService::checkProductDoesExitOrThrow")
                        .toException();
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
