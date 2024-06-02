package com.kzcse.springboot.product.api;

import com.kzcse.springboot.common.APIResponseDecorator;
import com.kzcse.springboot.product.data.entity.ProductEntity;
import com.kzcse.springboot.product.data.service.ProductListService;
import com.kzcse.springboot.product.domain.Product;
import com.kzcse.springboot.product.data.service.ProductDetailsService;
import com.kzcse.springboot.product.domain.model.response_model.ProductDetailsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * APIs:
 * <ul>
 *   <li>Product list</li>
 *   <li>Product details by ID</li>
 * </ul>
 */


@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductDetailsService productDetailsService;
    private final ProductListService productListService;

    public ProductController(ProductDetailsService productDetailsService, ProductListService productListService) {
        this.productDetailsService = productDetailsService;
        this.productListService = productListService;
    }

    // Error handling
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(NoSuchElementException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public ResponseEntity<Void> addProducts(@RequestBody List<ProductEntity> entities) {
        try {
            productListService.addProductsOrThrow(entities);
            return ResponseEntity.ok().build();
        } catch (Exception e) {

            return ResponseEntity.internalServerError().build();
        }

    }

    /**
     * <ul>
     *   <li>API that return all  of products list</li>
     * </ul>
     */
    @GetMapping("/all")
    public APIResponseDecorator<List<Product>> getAllProducts() {
        try {
            return new APIResponseDecorator<List<Product>>().onSuccess(productListService.getAllProducts());
        } catch (Exception e) {
            return new APIResponseDecorator<List<Product>>()
                    .withException(e, "Failed to add addDiscount By Price", "DiscountController::addDiscountByPrice");
        }

    }

    /**
     * <ul>
     *   <li>API that return details of product by id</li>
     * </ul>
     */

    @GetMapping("details/{id}")
    public APIResponseDecorator<ProductDetailsResponse> getProductDetails(@PathVariable String id) {
        try {
            var response = productDetailsService.fetchDetailsOrThrows(id);
            return new APIResponseDecorator<ProductDetailsResponse>().onSuccess(response);

        } catch (Exception e) {

            return new APIResponseDecorator<ProductDetailsResponse>()
                    .withException(e, "Failed to add addDiscount By Price", "DiscountController::addDiscountByPrice");

        }

    }


}
