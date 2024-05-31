package com.kzcse.springboot.api.product;

import com.kzcse.springboot.discount.data.DiscountByProductEntity;
import com.kzcse.springboot.enitity.entity.ProductEntity;
import com.kzcse.springboot.discount.data.DiscountByProductRepository;
import com.kzcse.springboot.enitity.repository.ProductRepository;
import com.kzcse.springboot.product.data.ProductDetailsService;
import com.kzcse.springboot.product.domain.ProductDetailsModel;
import com.kzcse.springboot.product.domain.ProductDetailsModelBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductRepository productRepository;
    private final DiscountByProductRepository byProductRepository;
    private final ProductDetailsService productDetailsService;

    public ProductController(ProductRepository productRepository, DiscountByProductRepository byProductRepository, ProductDetailsService productDetailsService) {
        this.productRepository = productRepository;
        this.byProductRepository = byProductRepository;
        this.productDetailsService = productDetailsService;
    }

    // Error handling
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(NoSuchElementException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Get all products
    @GetMapping
    public List<Product> getProducts() {
        var product = StreamSupport
                .stream(productRepository.findAll().spliterator(), false)
                .map(this::toProduct)
                .toList();
        // System.out.println("api/product::"+product);
        return product;
    }

    // Get product by id for description
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable String id) {

        var response = productRepository.findById(id);
        return response.map(this::toProduct).orElse(null);

    }

    @GetMapping("details/{id}")
    public ProductDetailsModel getProductDetails(@PathVariable String id) {
        //return  ProductDetailsModelBuilder.getDemoModel();
        return productDetailsService.fetchDetails(id);
    }


    @GetMapping("/offer/{id}")
    public DiscountByProductEntity getOffer(@PathVariable String id) {
        return StreamSupport
                .stream(byProductRepository.findAll().spliterator(), false)
                .filter(e -> Objects.equals(e.getParentId(), id))
                .findFirst()
                .orElse(null);

    }


    private Product toProduct(ProductEntity product) {
        return new Product(
                product.getPid(),
                product.getName(),
                List.of(product.getImageLink()),
                product.getPrice(),
                product.getDescription(),
                10
        );
    }
}
