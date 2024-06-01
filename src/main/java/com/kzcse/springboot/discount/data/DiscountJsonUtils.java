package com.kzcse.springboot.discount.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kzcse.springboot.discount.data.entity.DiscountByPriceEntity;
import com.kzcse.springboot.discount.data.entity.DiscountByProductEntity;
import com.kzcse.springboot.discount.data.repository.DiscountByPriceRepository;
import com.kzcse.springboot.discount.data.repository.DiscountByProductRepository;
import com.kzcse.springboot.discount.domain.DiscountByPriceRequestModel;
import com.kzcse.springboot.discount.domain.DiscountByProductRequestModel;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DiscountJsonUtils {


    public boolean addDiscountByProduct(DiscountByProductRepository discountByProductRepository) {
        var path = "src/main/resources/data/discount_by_product.json";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<DiscountByProductRequestModel> items = objectMapper.readValue(new File(path), new TypeReference<>() {
            });
//            System.out.println(items);

            discountByProductRepository.saveAll(items.stream().map(this::toModelByProduct).toList());
            return true;


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    public boolean addDiscountByPrice(DiscountByPriceRepository discountByPriceRepository) {
        var path = "src/main/resources/data/discount_by_price.json";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<DiscountByPriceRequestModel> items = objectMapper.readValue(new File(path), new TypeReference<>() {
            });
//            System.out.println(items);

            discountByPriceRepository.saveAll(items.stream().map(this::toModelByPrice).toList());
            return true;


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    private long getExpireTimeMs(int daysToAdd) {
        long currentTimeInMs = System.currentTimeMillis();
        long msInADay = TimeUnit.DAYS.toMillis(1);
        long addedTimeInMs = daysToAdd * msInADay;
        return currentTimeInMs + addedTimeInMs;
    }

    private DiscountByPriceEntity toModelByPrice(DiscountByPriceRequestModel model) {
        return new DiscountByPriceEntity(
                model.getProductId(),
                model.getAmount(),
                model.getExpirationTimeInMs()
        );
    }

    private DiscountByProductEntity toModelByProduct(DiscountByProductRequestModel model) {
        return new DiscountByProductEntity(
                model.getParentId() + model.getChildId(),
                model.getParentId(),
                model.getChildId(),
                model.getRequiredParentQuantity(),
                model.getFreeChildQuantity(),
                getExpireTimeMs(5)
        );
    }
}
