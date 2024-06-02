package com.kzcse.springboot.product.data.service;

import com.kzcse.springboot.common.APIResponseDecorator;
import com.kzcse.springboot.common.ErrorMessage;
import com.kzcse.springboot.inventory.data.InventoryEntity;
import com.kzcse.springboot.inventory.data.InventoryRepository;
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

    public void addProductsOrThrow(List<ProductEntity> entities) throws Exception {
        var iterator = productRepository.saveAll(entities);
        var savedEntities = StreamSupport
                .stream(iterator
                                .spliterator(),
                        false
                )
                .toList();
        var isNotSaved = savedEntities.size() != entities.size();
        if (isNotSaved) {
            throw new ErrorMessage()
                    .setMessage("failed to fetch save all")
                    .setCauses("Not all products were added successfully")
                    .setSource("ProductListService::addProductsOrThrow")
                    .toException();
        }


    }

    /**
     * <ul>
     *   <li>Fetch {@link ProductEntity}</li>
     *   <li>return {@link  APIResponseDecorator}  by wrapping {@link Product} for API response</li>
     *  <li>{@link Product} will directly convert to JSON in client side</li>
     * </ul>
     */
    public List<Product> getAllProducts() {
        return StreamSupport
                .stream(
                        productRepository
                                .findAll()
                                .spliterator(), false
                )
                .map(product -> toProduct(product, getAvailableQuantityOrZero(product.getPid())))
                .toList();
    }

    @SuppressWarnings("unused")
    public void checkAvailableQuantityOrThrow(ProductEntity entity) throws Exception {
        var quantityResponse = inventoryRepository.findById(entity.getPid());
        if (quantityResponse.isEmpty()) {
            throw new ErrorMessage()
                    .setMessage("failed to fetch details")
                    .setCauses("Product with id" + entity.getPid() + " is not found in Inventory")
                    .setSource("ProductListService::checkAvailableQuantityOrThrow")
                    .toException();
        }
    }

    private int getAvailableQuantityOrZero(String id) {
        var quantityResponse = inventoryRepository.findById(id);
        return quantityResponse.map(InventoryEntity::getQuantity).orElse(0);
    }

    /**
     * <ul>
     *   <li>Takes {@link ProductEntity}</li>
     *   <li>Fetch the availableAmount of that product {@link ProductEntity}</li>
     *   <li>Convert {@link ProductEntity} to {@link  Product } for API response</li>
     *  <li>{@link Product} will directly convert to JSON in client side</li>
     * </ul>
     */
    private Product toProduct(ProductEntity product, int availableQuantity) {
        return new Product(
                product.getPid(),
                product.getName(),
                List.of(product.getImageLink()),
                product.getPrice(),
                product.getDescription(),
                availableQuantity
        );
    }
}
