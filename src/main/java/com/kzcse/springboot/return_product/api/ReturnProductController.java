package com.kzcse.springboot.return_product.api;

import com.kzcse.springboot.common.APIResponseDecorator;
import com.kzcse.springboot.inventory.data.InventoryRepository;
import com.kzcse.springboot.return_product.data.entiry.ReturningPendingEntity;
import com.kzcse.springboot.return_product.data.repository.ReturnProductRepository;
import com.kzcse.springboot.return_product.data.service.ProductReturnService;
import com.kzcse.springboot.return_product.domain.ReturnRequest;
import com.kzcse.springboot.return_product.domain.ReturningPendingProductModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/product/return")
public class ReturnProductController {
    private final InventoryRepository inventoryRepository;
    private final ReturnProductRepository returnProductRepository;
    private final ProductReturnService productReturnService;

    public ReturnProductController(InventoryRepository inventoryRepository, ReturnProductRepository returnProductRepository, ProductReturnService productReturnService) {
        this.inventoryRepository = inventoryRepository;
        this.returnProductRepository = returnProductRepository;
        this.productReturnService = productReturnService;
    }

    // Error handling
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(NoSuchElementException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @PostMapping("/request")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public APIResponseDecorator<String> returnRequest(@RequestBody ReturnRequest request) {
        try {
            return new APIResponseDecorator<String>().onSuccess(productReturnService.createReturnRequestOrThrow(request).getMessage());
        } catch (Exception e) {
            return new APIResponseDecorator<String>().withException(e, "Request failed", this.getClass().getSimpleName());
        }

    }

    // Get all products
    @GetMapping("/pending")
    public List<ReturningPendingProductModel> getProducts() {
        return StreamSupport
                .stream(returnProductRepository.findAll().spliterator(), false)
                .map(this::toModel)
                .toList();
    }


    @PostMapping("/confirm/{id}")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public boolean returnItem(@PathVariable String id) {
        try {
            var entity = returnProductRepository.findById(id).orElse(null);
            if (entity != null) {
                inventoryRepository.addQuantity(entity.getPurchasedProductId(), entity.getReturnQuantity());
                if (entity.getFreeProductId() != null) {
                    inventoryRepository.addQuantity(entity.getFreeProductId(), entity.getFreeItemQuantity());
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private ReturningPendingProductModel toModel(ReturningPendingEntity entity) {
        return new ReturningPendingProductModel(entity.getId(), entity.getFreeProductId(), entity.getReturnQuantity(), entity.getFreeItemQuantity());
    }


}
