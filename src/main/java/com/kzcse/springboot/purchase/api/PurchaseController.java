package com.kzcse.springboot.purchase.api;

import com.kzcse.springboot.common.APIResponseDecorator;
import com.kzcse.springboot.purchase.data.service.ProductOrderConfirmService;
import com.kzcse.springboot.purchase.data.service.ProductOrderRequestService;
import com.kzcse.springboot.purchase.data.service.PurchasedHistoryService;
import com.kzcse.springboot.purchase.domain.request_model.OrderRequest;
import com.kzcse.springboot.purchase.domain.response_model.OrderBillResponse;
import com.kzcse.springboot.purchase.domain.response_model.PurchasedProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {
    private final ProductOrderConfirmService orderConfirmService;
    private final PurchasedHistoryService purchasedHistoryService;
    private final ProductOrderRequestService orderRequestService;


    public PurchaseController(ProductOrderConfirmService orderConfirmService, PurchasedHistoryService purchasedHistoryService, ProductOrderRequestService orderRequestService) {
        this.orderConfirmService = orderConfirmService;
        this.purchasedHistoryService = purchasedHistoryService;

        this.orderRequestService = orderRequestService;
    }

    // Error handling
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(NoSuchElementException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @PostMapping("/request")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public APIResponseDecorator<OrderBillResponse> orderRequest(@RequestBody OrderRequest request) {
        try {
            return new APIResponseDecorator<OrderBillResponse>().onSuccess(orderRequestService.processRequestOrThrow(request));

        }
        catch (Exception e){
            return new APIResponseDecorator<OrderBillResponse>().withException(e, "failed to purchase", "PurchaseController::orderConfirm");
        }
    }

    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public APIResponseDecorator<List<String>> orderConfirm(@RequestBody OrderRequest request) {
        try {
            return new APIResponseDecorator<List<String>>().onSuccess(orderConfirmService.confirmOrThrow(request));
        } catch (Exception e) {
            return new APIResponseDecorator<List<String>>().withException(e, "failed to purchase", "PurchaseController::orderConfirm");
        }
    }


    //return the purchased product of user
    @GetMapping("/{userId}")
    public APIResponseDecorator<List<PurchasedProductResponse>> getProduct(@PathVariable String userId) {
        try {
            return new APIResponseDecorator<List<PurchasedProductResponse>>()
                    .onSuccess(purchasedHistoryService.getPurchasedProductOrThrow(userId));
        } catch (Exception e) {
            return new APIResponseDecorator<List<PurchasedProductResponse>>().withException(e, "failed", "PurchaseController::getProduct");
        }
    }
}
