package com.kzcse.springboot.return_product.api;

import com.kzcse.springboot.common.APIResponseDecorator;
import com.kzcse.springboot.return_product.data.entiry.PendingReturnProduct;
import com.kzcse.springboot.return_product.data.repository.ReturnProductRepository;
import com.kzcse.springboot.return_product.data.service.ReturnRequestAcceptService;
import com.kzcse.springboot.return_product.data.service.ReturnRequestSendService;
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
    private final ReturnProductRepository returnProductRepository;
    private final ReturnRequestSendService returnRequestSendService;
    private final ReturnRequestAcceptService returnRequestAcceptService;

    public ReturnProductController(ReturnProductRepository returnProductRepository, ReturnRequestSendService returnRequestSendService, ReturnRequestAcceptService returnRequestAcceptService) {
        this.returnProductRepository = returnProductRepository;
        this.returnRequestSendService = returnRequestSendService;
        this.returnRequestAcceptService = returnRequestAcceptService;
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
            return new APIResponseDecorator<String>().onSuccess(returnRequestSendService.createReturnRequestOrThrow(request).getMessage());
        } catch (Exception e) {
            return new APIResponseDecorator<String>().withException(e, "Request failed", this.getClass().getSimpleName());
        }

    }

    // Get all products
    @GetMapping("/pending")
    public APIResponseDecorator<List<ReturningPendingProductModel>> getProducts() {
        try {
            return new APIResponseDecorator<List<ReturningPendingProductModel>>()
                    .onSuccess(StreamSupport
                            .stream(returnProductRepository.findAll().spliterator(), false)
                            .map(this::toModel)
                            .toList());
        } catch (Exception e) {
            return new APIResponseDecorator<List<ReturningPendingProductModel>>()
                    .withException(e, "Failed", getClass().getSimpleName());

        }

    }


    @PostMapping("/confirm/{purchaseId}")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public APIResponseDecorator<String> returnItem(@PathVariable String purchaseId) {
        try {
            returnRequestAcceptService.acceptRequestOrThrow(purchaseId);
            return new APIResponseDecorator<String>().onSuccess("Success");

        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponseDecorator<String>().withException(e, "Failed", this.getClass().getSimpleName());
        }

    }

    private ReturningPendingProductModel toModel(PendingReturnProduct entity) {
        return new ReturningPendingProductModel(entity.getId(), entity.getBonusProductId(), entity.getMainProductReturnQuantity(), entity.getBonusReturnQuantity());
    }


}
