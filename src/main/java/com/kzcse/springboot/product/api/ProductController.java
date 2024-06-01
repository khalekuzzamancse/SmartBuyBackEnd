package com.kzcse.springboot.product.api;

import com.kzcse.springboot.common.APIResponseDecorator;
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

    /**
     * <ul>
     *   <li>API that return all  of products list</li>
     * </ul>
     */
    @GetMapping("/all")
    public APIResponseDecorator<List<Product>> getAllProducts() {
        try {
            return productListService.getAllProducts();
        } catch (Exception e) {
//            e.printStackTrace();
            return new APIResponseDecorator<List<Product>>()
                    .onFailure("ServerError::ProductController::getAllProducts()" + e.getMessage());
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
            var response = productDetailsService.fetchDetails(id);
            if (response != null) {
                return response;
            } else {
                return new APIResponseDecorator<ProductDetailsResponse>()
                        .onFailure("Server Error:ProductController::getProductDetails():Product Details not found");

            }
        } catch (Exception e) {
            //e.printStackTrace();
            return new APIResponseDecorator<ProductDetailsResponse>()
                    .onFailure("Server Error:" + e.getMessage());
        }

    }


}


//    @GetMapping("/offer/{id}")
//    public DiscountByProductEntity getOffer(@PathVariable String id) {
//        return StreamSupport
//                .stream(byProductRepository.findAll().spliterator(), false)
//                .filter(e -> Objects.equals(e.getParentId(), id))
//                .findFirst()
//                .orElse(null);
//
//    }

// Get product by id for description
//    @GetMapping("/{id}")
//    public APIResponseDecorator<Product> getProduct(@PathVariable String id) {
//        try {
//            var response = productRepository.findById(id);
//            if (response.isPresent())
//                return new APIResponseDecorator<Product>().onSuccess(toProduct(response.get()));
//            else
//                return new APIResponseDecorator<Product>().onFailure("Server Error:Product not found");
//
//        } catch (Exception e) {
//            //e.printStackTrace();
//            return new APIResponseDecorator<Product>().onFailure("Server Error:" + e.getMessage());
//        }
//
//
//    }