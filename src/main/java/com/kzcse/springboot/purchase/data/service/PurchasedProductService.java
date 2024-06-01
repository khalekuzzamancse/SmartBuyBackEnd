package com.kzcse.springboot.purchase.data.service;

import com.kzcse.springboot.product.data.repository.ProductRepository;
import com.kzcse.springboot.purchase.data.repositoy.PurchasedProductRepository;
import com.kzcse.springboot.purchase.domain.response_model.PurchasedProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class PurchasedProductService {
    private final ProductRepository productRepository;
    private final PurchasedProductRepository purchasedProductRepository;

    public PurchasedProductService(ProductRepository productRepository, PurchasedProductRepository purchasedProductRepository) {
        this.productRepository = productRepository;
        this.purchasedProductRepository = purchasedProductRepository;
    }

    public List<PurchasedProductResponse> getPurchasedProduct(@PathVariable String userId) {
        List<PurchasedProductResponse> responses = new java.util.ArrayList<>(List.of());

        purchasedProductRepository
                .findByUserId(userId)
                .forEach(purchased -> {
                    var productResponse = productRepository.findById(purchased.getProductId());
                    if (productResponse.isPresent()) {
                        var product = productResponse.get();
                        var response = new PurchasedProductResponse(
                                purchased.getId(),
                                product.getName(),
                                product.getImageLink(),
                                purchased.getQuantity(),
                                purchased.getReturnExpireDate().toString()

                        );
                        responses.add(response);
                    }
                });

        return responses;
    }


}
