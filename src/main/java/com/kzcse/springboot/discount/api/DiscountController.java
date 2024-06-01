package com.kzcse.springboot.discount.api;

import com.kzcse.springboot.common.APIResponseDecorator;
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

    @PostMapping("add/by-product")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public APIResponseDecorator<String> addDiscountByProduct(@RequestBody List<DiscountByProductRequestModel> discounts) {
        try {
            return discountByProductService.addDiscount(discounts);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponseDecorator<String>()
                    .onFailure("ServerError::DiscountController::addDiscountByProduct" + e.getMessage());
        }

    }

    @PostMapping("add/by-price")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public APIResponseDecorator<String> addDiscountByPrice(@RequestBody List<DiscountByPriceRequestModel> entities) {
        try {
            return discountByPriceService.addDiscountByPrice(entities);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponseDecorator<String>()
                    .onFailure(e.getMessage());
        }

    }


}
