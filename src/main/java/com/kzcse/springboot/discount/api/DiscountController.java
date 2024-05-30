package com.kzcse.springboot.discount.api;

import com.kzcse.springboot.discount.data.DiscountByProductEntity;
import com.kzcse.springboot.discount.data.DiscountByProductRepository;
import com.kzcse.springboot.discount.domain.DiscountByProductModel;
import com.kzcse.springboot.enitity.repository.InventoryRepository;
import com.kzcse.springboot.purchase.repositoy.PurchasedProductRepository;
import com.kzcse.springboot.return_product.data.repository.ReturnProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/product/discount")
public class DiscountController {
    private final DiscountByProductRepository discountByProductRepository;

    public DiscountController(InventoryRepository inventoryRepository, DiscountByProductRepository discountByProductRepository, PurchasedProductRepository purchasedProductRepository, ReturnProductRepository returnProductRepository) {
        this.discountByProductRepository = discountByProductRepository;
    }

    // Error handling
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(NoSuchElementException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public boolean addDiscount(@RequestBody List<DiscountByProductModel> discounts) {
        try {
            var entities=discounts.stream().map(this::toModel).toList();
            System.out.println(entities);
            discountByProductRepository.saveAll(entities);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private DiscountByProductEntity toModel(DiscountByProductModel model) {
        return new DiscountByProductEntity(
                model.getParentId() + model.getChildId(),
                model.getParentId(),
                model.getChildId(),
                model.getRequiredParentQuantity(),
                model.getFreeChildQuantity());
    }


}
