package com.kzcse.springboot.discount.api;

import com.kzcse.springboot.discount.data.entity.DiscountByPriceEntity;
import com.kzcse.springboot.discount.data.repository.DiscountByPriceRepository;
import com.kzcse.springboot.discount.data.entity.DiscountByProductEntity;
import com.kzcse.springboot.discount.data.repository.DiscountByProductRepository;
import com.kzcse.springboot.discount.domain.DiscountByPriceRequestModel;
import com.kzcse.springboot.discount.domain.DiscountByProductRequestModel;
import com.kzcse.springboot.product.data.InventoryRepository;
import com.kzcse.springboot.purchase.repositoy.PurchasedProductRepository;
import com.kzcse.springboot.return_product.data.repository.ReturnProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/product/discount")
public class DiscountController {
    private final DiscountByProductRepository discountByProductRepository;
    private final DiscountByPriceRepository discountByPriceRepository;

    public DiscountController(InventoryRepository inventoryRepository, DiscountByProductRepository discountByProductRepository, PurchasedProductRepository purchasedProductRepository, ReturnProductRepository returnProductRepository, DiscountByPriceRepository discountByPriceRepository) {
        this.discountByProductRepository = discountByProductRepository;
        this.discountByPriceRepository = discountByPriceRepository;
    }

    // Error handling
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(NoSuchElementException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @PostMapping("byproduct/add")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public boolean addDiscount(@RequestBody List<DiscountByProductRequestModel> discounts) {
        try {
            var entities = discounts.stream().map(this::toModel).toList();
            System.out.println(entities);
            discountByProductRepository.saveAll(entities);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @PostMapping("by-price/add")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public boolean addDiscountByPrice(@RequestBody List<DiscountByPriceRequestModel> entities) {
        try {
            discountByPriceRepository.saveAll(entities.stream().map(this::toModelPrice).toList());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private DiscountByPriceEntity toModelPrice(DiscountByPriceRequestModel model) {
        var entity = new DiscountByPriceEntity();
        entity.setProductId(model.getProductId());
        entity.setExpirationTimeInMs(model.getExpirationTimeInMs());
        entity.setAmount(model.getAmount());
        return entity;
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

    private long getExpireTimeMs(int daysToAdd) {
        long currentTimeInMs = System.currentTimeMillis();
        long msInADay = TimeUnit.DAYS.toMillis(1);
        long addedTimeInMs = daysToAdd * msInADay;
        return currentTimeInMs + addedTimeInMs;
    }


}
