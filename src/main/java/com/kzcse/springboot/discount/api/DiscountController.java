package com.kzcse.springboot.discount.api;

import com.kzcse.springboot.common.APIResponseDecorator;
import com.kzcse.springboot.discount.data.entity.DiscountByProductEntity;
import com.kzcse.springboot.discount.data.service.DiscountByPriceService;
import com.kzcse.springboot.discount.data.service.DiscountByProductService;
import com.kzcse.springboot.discount.domain.DiscountByPriceRequestModel;
import com.kzcse.springboot.discount.domain.DiscountByProductRequestModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/product/discount")
public class DiscountController {
    private final DiscountByPriceService discountByPriceService;
    private final DiscountByProductService discountByProductService;


    public DiscountController(DiscountByPriceService discountByPriceService, DiscountByProductService discountByProductService) {
        this.discountByPriceService = discountByPriceService;
        this.discountByProductService = discountByProductService;
    }

    // Error handling
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(NoSuchElementException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @GetMapping("all/by-product")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public APIResponseDecorator<List<DiscountByProductEntity>> allDiscountByProduct() {
        try {

            return new APIResponseDecorator<List<DiscountByProductEntity>>().onSuccess(discountByProductService.getAllDiscount());
        } catch (Exception e) {
            return new APIResponseDecorator<List<DiscountByProductEntity>>().withException(e, "failed to read", this.getClass().getSimpleName()+"::addDiscountByProduct");

        }

    }
    @PostMapping("add/by-product")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public APIResponseDecorator<String> addDiscountByProduct(@RequestBody List<DiscountByProductRequestModel> discounts) {
        try {
            discountByProductService.addDiscountOrThrow(discounts);
            return new APIResponseDecorator<String>().onSuccess("Success");
        } catch (Exception e) {
            return new APIResponseDecorator<String>().withException(e, "failed to add", this.getClass().getSimpleName()+"::addDiscountByProduct");

        }

    }

    @PostMapping("add/by-price")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public APIResponseDecorator<String> addDiscountByPrice(@RequestBody List<DiscountByPriceRequestModel> entities) {
        try {
            discountByPriceService.addDiscountByPriceOrThrow(entities);
            return new APIResponseDecorator<String>().onSuccess("Success");
        } catch (Exception e) {
            return new APIResponseDecorator<String>().withException(e, "failed to add", "DiscountController::addDiscountByPrice");
        }

    }


}
