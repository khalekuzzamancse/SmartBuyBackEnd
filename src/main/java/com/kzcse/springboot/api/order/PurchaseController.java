package com.kzcse.springboot.api.order;

import com.kzcse.springboot.enitity.repository.DiscountByProductRepository;
import com.kzcse.springboot.enitity.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {
    private final ProductRepository productRepository;

    public PurchaseController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Error handling
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(NoSuchElementException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @PostMapping("/request")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public OrderResponse orderRequest(@RequestBody OrderRequest request) {
        AtomicInteger total = new AtomicInteger();
        request.getItems().forEach(item -> {
            var response = productRepository.findById(item.getProductId());
            response.ifPresent(productEntity -> total.addAndGet(productEntity.getPrice() * item.getQuantity()));
        });

        System.out.println(request);
        return new OrderResponse(total.get());
    }

    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public boolean orderConfirm(@RequestBody OrderRequest request) {

        //TODO:Update product inventory:child product and parent product
        //TODO:


        System.out.println(request);
        return true;
    }


}
