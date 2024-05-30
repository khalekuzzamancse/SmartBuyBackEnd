package com.kzcse.springboot.purchase.api;

import com.kzcse.springboot.enitity.repository.DiscountByProductRepository;
import com.kzcse.springboot.purchase.model.OrderRequest;
import com.kzcse.springboot.purchase.model.OrderResponse;
import com.kzcse.springboot.purchase.entity.PurchasedProductEntity;
import com.kzcse.springboot.purchase.model.PurchasedResponse;
import com.kzcse.springboot.purchase.model.ReturnRequest;
import com.kzcse.springboot.enitity.repository.InventoryRepository;
import com.kzcse.springboot.enitity.repository.ProductRepository;
import com.kzcse.springboot.purchase.repositoy.PurchasedProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final DiscountByProductRepository discountByProductRepository;
    private final PurchasedProductRepository purchasedProductRepository;

    public PurchaseController(ProductRepository productRepository, InventoryRepository inventoryRepository, DiscountByProductRepository discountByProductRepository, PurchasedProductRepository purchasedProductRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.discountByProductRepository = discountByProductRepository;
        this.purchasedProductRepository = purchasedProductRepository;
    }

    // Error handling
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(NoSuchElementException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @PostMapping("/request")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public OrderResponse orderRequest(@RequestBody OrderRequest request) {
        AtomicInteger total = new AtomicInteger();
        request.getItems().forEach(item -> {
            var response = productRepository.findById(item.getProductId());
            response.ifPresent(productEntity -> total.addAndGet(productEntity.getPrice() * item.getQuantity()));
        });

        System.out.println(request);
        return new OrderResponse(total.get());
    }

    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public List<PurchasedResponse> orderConfirm(@RequestBody OrderRequest request) {
        try {
            List<PurchasedResponse> purchaseIds = new java.util.ArrayList<>(List.of());
            request.getItems().forEach(item -> {
                var purchaseId = item.getProductId() + request.getUserId();
                var discountId = discountByProductRepository.findDiscountId(item.getProductId(), item.getQuantity());
                var expireDate = LocalDate.now();
                var purchase = new PurchasedProductEntity(purchaseId, request.getUserId(), item.getProductId(), item.getQuantity(), discountId, expireDate);

                var response = purchasedProductRepository.save(purchase);
                var isSuccess = (purchase.equals(response));
                if (isSuccess) {
                    inventoryRepository.subtractQuantity(item.getProductId(), item.getQuantity());
                    purchaseIds.add(new PurchasedResponse(purchaseId, discountId, expireDate));
                    if (discountId != null) {
                        var discount = discountByProductRepository.findById(discountId).orElse(null);
                        if (discount != null) {
                            var childId = discount.getChildId();
                            var quantity = discount.getFreeChildQuantity();
                            inventoryRepository.subtractQuantity(childId, quantity);
                        }
                        System.out.println(purchase);
                    }

                }
            });
            //return list of purchase id
            return purchaseIds;
        } catch (Exception e) {
            System.out.println(e);
            return Collections.emptyList();
        }
    }

    @PostMapping("/return")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public boolean returnItem(@RequestBody ReturnRequest request) {
        try {
            var purchasedProduct = purchasedProductRepository.findById(request.getPurchaseId()).orElse(null);
            assert purchasedProduct != null;
            var productId = purchasedProduct.getProductId();
            inventoryRepository.addQuantity(productId, request.getReturnQuantity());
            var discountId = purchasedProduct.getDiscountId();
            var wasDiscount = discountId != null;
            if (wasDiscount) {
                var itemAfterReturn = purchasedProduct.getQuantity() - request.getReturnQuantity();
                var discount = discountByProductRepository.findById(discountId).orElse(null);
                assert discount != null;
                var noMoreEligibleForDiscount = itemAfterReturn < discount.getRequiredParentQuantity();
                if (noMoreEligibleForDiscount) {
                    var offeredProductId = discount.getChildId();
                    var offeredProductAmount = discount.getFreeChildQuantity();
                    inventoryRepository.addQuantity(offeredProductId, offeredProductAmount);
                }

            }
            return  true;
        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }

    }


}
