package com.kzcse.springboot.purchase.api;

import com.kzcse.springboot.common.APIResponseDecorator;
import com.kzcse.springboot.purchase.data.service.ProductOrderService;
import com.kzcse.springboot.purchase.data.service.PurchasedProductService;
import com.kzcse.springboot.purchase.domain.request_model.OrderRequest;
import com.kzcse.springboot.purchase.domain.response_model.PurchasedProductResponse;
import com.kzcse.springboot.product.data.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {
    private final ProductRepository productRepository;
    private final ProductOrderService productOrderService;
    private final PurchasedProductService purchasedProductService;

    public PurchaseController(ProductRepository productRepository, ProductOrderService productOrderService, PurchasedProductService purchasedProductService) {
        this.productRepository = productRepository;
        this.productOrderService = productOrderService;
        this.purchasedProductService = purchasedProductService;
    }

    // Error handling
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(NoSuchElementException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

//    @PostMapping("/request")
//    @ResponseStatus(HttpStatus.CREATED) // response code for success
//    public OrderResponse orderRequest(@RequestBody OrderRequest request) {
//        AtomicInteger total = new AtomicInteger();
//        request.getItems().forEach(item -> {
//            var response = productRepository.findById(item.getProductId());
//            response.ifPresent(productEntity -> total.addAndGet(productEntity.getPrice() * item.getQuantity()));
//        });
//
//        System.out.println(request);
//        return new OrderResponse(total.get());
//    }

    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public APIResponseDecorator<String> orderConfirm(@RequestBody OrderRequest request) {
        try {
            productOrderService.orderConfirmOrThrow(request);
            return new APIResponseDecorator<String>().onSuccess("Successfully purchases");
        } catch (Exception e) {
            return new APIResponseDecorator<String>().withException(e,"failed to purchase","PurchaseController::orderConfirm");
        }
    }


    //return the purchased product of user
    @GetMapping("/{userId}")
    public APIResponseDecorator<List<PurchasedProductResponse>> getProduct(@PathVariable String userId) {
        try {
            return new APIResponseDecorator<List<PurchasedProductResponse>>()
                    .onSuccess(purchasedProductService.getPurchasedProductOrThrow(userId));
        } catch (Exception e) {
            return new APIResponseDecorator<List<PurchasedProductResponse>>().withException(e,"failed","PurchaseController::getProduct");
        }
    }
}
