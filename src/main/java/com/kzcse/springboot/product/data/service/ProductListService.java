package com.kzcse.springboot.product.data.service;

import com.kzcse.springboot.common.APIResponseDecorator;
import com.kzcse.springboot.product.data.repository.InventoryRepository;
import com.kzcse.springboot.product.data.entity.ProductEntity;
import com.kzcse.springboot.product.data.repository.ProductRepository;
import com.kzcse.springboot.product.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Responsibility:
 * <ul>
 *   <li>Fetch the product list from {@link ProductRepository}</li>
 *   <li>Fetch product availableAmount from {@link InventoryRepository}</li>
 *   <li>Make the {@link Product} model by merging product info and availableAmount</li>
 *   <li>{@link Product} will directly convert to JSON in client side</li>
 * </ul>
 */

@Service
public class ProductListService {
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;


    public ProductListService(ProductRepository productRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }


    /**
     * <ul>
     *   <li>Fetch {@link ProductEntity}</li>
     *   <li>return {@link  APIResponseDecorator}  by wrapping {@link Product} for API response</li>
     *  <li>{@link Product} will directly convert to JSON in client side</li>
     * </ul>
     */
    public APIResponseDecorator<List<Product>> getAllProducts() throws Exception {
        try {
            var products = StreamSupport
                    .stream(productRepository.findAll().spliterator(), false)
                    .map(product -> {
                        try {
                            return toProduct(product);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
            return new APIResponseDecorator<List<Product>>().onSuccess(products);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof Exception) {
                throw (Exception) e.getCause();
            } else {
                throw e;
            }
        }
    }

    /**
     * <ul>
     *   <li>Takes {@link ProductEntity}</li>
     *   <li>Fetch the availableAmount of that product {@link ProductEntity}</li>
     *   <li>Convert {@link ProductEntity} to {@link  Product } for API response</li>
     *  <li>{@link Product} will directly convert to JSON in client side</li>
     * </ul>
     */


    private Product toProduct(ProductEntity product) throws Exception {
        var quantityResponse = inventoryRepository.findById(product.getPid());
        if (quantityResponse.isEmpty())
            throw new Exception("Product with id " + product.getPid() + " not found");
        return new Product(
                product.getPid(),
                product.getName(),
                List.of(product.getImageLink()),
                product.getPrice(),
                product.getDescription(),
                quantityResponse.get().getQuantity()
        );
    }
}
