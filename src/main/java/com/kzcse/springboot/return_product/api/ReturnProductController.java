package com.kzcse.springboot.return_product.api;

import com.kzcse.springboot.discount.data.DiscountByProductRepository;
import com.kzcse.springboot.enitity.repository.InventoryRepository;
import com.kzcse.springboot.purchase.repositoy.PurchasedProductRepository;
import com.kzcse.springboot.return_product.data.entiry.ReturningPendingEntity;
import com.kzcse.springboot.return_product.data.repository.ReturnProductRepository;
import com.kzcse.springboot.return_product.domain.ReturnRequest;
import com.kzcse.springboot.return_product.domain.ProductReturnResponseModel;
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
    private final InventoryRepository inventoryRepository;
    private final DiscountByProductRepository discountByProductRepository;
    private final PurchasedProductRepository purchasedProductRepository;
    private final ReturnProductRepository returnProductRepository;

    public ReturnProductController(InventoryRepository inventoryRepository, DiscountByProductRepository discountByProductRepository, PurchasedProductRepository purchasedProductRepository, ReturnProductRepository returnProductRepository) {
        this.inventoryRepository = inventoryRepository;
        this.discountByProductRepository = discountByProductRepository;
        this.purchasedProductRepository = purchasedProductRepository;
        this.returnProductRepository = returnProductRepository;
    }

    // Error handling
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(NoSuchElementException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @PostMapping("/request")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public ProductReturnResponseModel returnRequest(@RequestBody ReturnRequest request) {
        try {
            var purchasedProduct = purchasedProductRepository.findById(request.getPurchaseId()).orElse(null);
            assert purchasedProduct != null;
            var productId = purchasedProduct.getProductId();
            var discountId = purchasedProduct.getDiscountId();
            var wasDiscount = discountId != null;
            if (wasDiscount) {
                var itemAfterReturn = purchasedProduct.getQuantity() - request.getReturnQuantity();
                var discount = discountByProductRepository.findById(discountId).orElse(null);
                assert discount != null;
                var noMoreEligibleForDiscount = itemAfterReturn < discount.getRequiredParentQuantity();
                if (noMoreEligibleForDiscount) {
                    var offeredProductAmount = discount.getFreeChildQuantity();
                    var offeredProductId = discount.getChildId();

                    //save to database
                    returnProductRepository.save(
                            new ReturningPendingEntity(
                                    request.getPurchaseId(),
                                    productId,
                                    offeredProductId,
                                    request.getReturnQuantity(),
                                    offeredProductAmount)
                    );

                    return new ProductReturnResponseModel(
                            "You have to return " + offeredProductAmount + " offered product(s) also."
                    );
                }

            }
            //save to database
            returnProductRepository.save(
                    new ReturningPendingEntity(
                            request.getPurchaseId(),
                            productId,
                            null,
                            request.getReturnQuantity(),
                            0)
            );
            return new ProductReturnResponseModel(null
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    // Get all products
    @GetMapping("/pending")
    public List<ReturningPendingProductModel> getProducts() {
        return StreamSupport
                .stream(returnProductRepository.findAll().spliterator(), false)
                .map(this::toModel)
                .toList();
    }


    @PostMapping("/confirm/{id}")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public boolean returnItem(@PathVariable String id) {
        try {
            var entity = returnProductRepository.findById(id).orElse(null);
            if (entity != null) {
                inventoryRepository.addQuantity(entity.getPurchasedProductId(), entity.getPurchaseQuantity());
                if (entity.getFreeProductId() != null) {
                    inventoryRepository.addQuantity(entity.getFreeProductId(), entity.getFreeItemQuantity());
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private ReturningPendingProductModel toModel(ReturningPendingEntity entity) {
        return new ReturningPendingProductModel(entity.getId(), entity.getFreeProductId(), entity.getPurchaseQuantity(), entity.getFreeItemQuantity());
    }


}
