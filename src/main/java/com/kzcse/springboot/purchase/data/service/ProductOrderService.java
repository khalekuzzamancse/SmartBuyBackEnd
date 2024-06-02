package com.kzcse.springboot.purchase.data.service;

import com.kzcse.springboot.auth.data.service.UserService;
import com.kzcse.springboot.common.ErrorMessage;
import com.kzcse.springboot.discount.data.repository.DiscountByProductRepository;
import com.kzcse.springboot.inventory.data.InventoryRepository;
import com.kzcse.springboot.product.data.repository.ProductRepository;
import com.kzcse.springboot.purchase.data.entity.PurchasedProductEntity;
import com.kzcse.springboot.purchase.data.repositoy.PurchasedProductRepository;
import com.kzcse.springboot.purchase.domain.request_model.OrderRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProductOrderService {
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final DiscountByProductRepository discountByProductRepository;
    private final PurchasedProductRepository purchasedProductRepository;
    private final UserService userService;


    public ProductOrderService(ProductRepository productRepository, InventoryRepository inventoryRepository, DiscountByProductRepository discountByProductRepository, PurchasedProductRepository purchasedProductRepository, UserService userService) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.discountByProductRepository = discountByProductRepository;
        this.purchasedProductRepository = purchasedProductRepository;
        this.userService = userService;
    }

    public void orderConfirmOrThrow(OrderRequest request) throws Exception {
        throwIfUserDoesNotExit(request.getUserId());
        throwIfProductDoesNotExits(request);

        for (var item : request.getItems()) {
            var purchaseId = item.getProductId() + request.getUserId();
            var discountIds = discountByProductRepository.findDiscountProductId(item.getProductId(), item.getQuantity());
            String takenDiscountId = null;
            if (!discountIds.isEmpty()) {
                takenDiscountId = discountIds.get(discountIds.size() - 1); //taken the last once
            }
            var expireDate = LocalDate.now();
            var entity = new PurchasedProductEntity(purchaseId, request.getUserId(), item.getProductId(), item.getQuantity(), takenDiscountId, expireDate);

            var response = purchasedProductRepository.save(entity);
            var isSuccess = (entity.equals(response));
            if (!isSuccess) {
                throw new ErrorMessage()
                        .setMessage("Failed; Product"  + item.getProductId() +" has not been purchased")
                        .setCauses("Failed to save database")
                        .setSource("ProductOrderService::orderConfirmOrThrow")
                        .toException();
            }

            subtractProductQuantityFromDBOrThrow(item.getProductId(), item.getQuantity());
            if (takenDiscountId != null) {
                System.out.println("YEST");
                var discount = discountByProductRepository.findById(takenDiscountId).orElse(null);
                if (discount != null) {
                    var childId = discount.getChildId();
                    var quantity = discount.getFreeChildQuantity();
                    subtractProductQuantityFromDBOrThrow(childId, quantity);
                }
            }

        }
    }

    private void throwIfUserDoesNotExit(String userId) throws Exception {
        if (userService.doesNotExit(userId)) {
            throw new ErrorMessage()
                    .setMessage("failed")
                    .setCauses("User does not Exits")
                    .setSource("ProductOrderService::throwIfUserDoesNotExit")
                    .toException();
        }

    }

    private void subtractProductQuantityFromDBOrThrow(String productId, int amountToSubtract) throws Exception {
        var rowAffected = inventoryRepository.subtractQuantity(productId, amountToSubtract);
        var isNotSuccess = !(rowAffected > 0);
        if (isNotSuccess) {
            throw new ErrorMessage()
                    .setMessage("failed")
                    .setCauses("Product with id "+productId+" +did not subtracted from databases,because no row updated in database table")
                    .setSource("ProductOrderService::subtractProductQuantityFromDBOrThrow")
                    .toException();

        }

    }

    private void throwIfProductDoesNotExits(OrderRequest request) throws Exception {
        for (var requestModel : request.getItems()) {
            var productId=requestModel.getProductId();
            if (!productRepository.existsById(productId)) {
                throw new ErrorMessage()
                        .setMessage("failed")
                        .setCauses("Product with id " +productId+" does not exits")
                        .setSource("ProductOrderService::throwIfProductDoesNotExits")
                        .toException();
            }
        }

    }
}
