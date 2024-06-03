package com.kzcse.springboot.purchase.data.service;

import com.kzcse.springboot.auth.data.service.UserService;
import com.kzcse.springboot.auth.domain.AuthFactory;
import com.kzcse.springboot.common.ErrorMessage;
import com.kzcse.springboot.discount.data.repository.DiscountByProductRepository;
import com.kzcse.springboot.inventory.data.InventoryRepository;
import com.kzcse.springboot.inventory.domain.InventoryFactory;
import com.kzcse.springboot.product.data.repository.ProductRepository;
import com.kzcse.springboot.product.domain.ProductFactory;
import com.kzcse.springboot.purchase.data.entity.PurchasedProductEntity;
import com.kzcse.springboot.purchase.data.repositoy.PurchasedProductRepository;
import com.kzcse.springboot.purchase.domain.request_model.OrderRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class ProductOrderConfirmService {
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final DiscountByProductRepository discountByProductRepository;
    private final PurchasedProductRepository purchasedProductRepository;
    private final AuthFactory authFactory;
    private final InventoryFactory inventoryFactory;
    private final ProductFactory productFactory;


    public ProductOrderConfirmService(ProductRepository productRepository, InventoryRepository inventoryRepository, DiscountByProductRepository discountByProductRepository, PurchasedProductRepository purchasedProductRepository, UserService userService, AuthFactory authFactory, InventoryFactory inventoryFactory, ProductFactory productFactory) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.discountByProductRepository = discountByProductRepository;
        this.purchasedProductRepository = purchasedProductRepository;
        this.authFactory = authFactory;
        this.inventoryFactory = inventoryFactory;
        this.productFactory = productFactory;
    }

    public List<String> confirmOrThrow(OrderRequest request) throws Exception {


        throwIfUserDoesNotExit(request.getUserId());
        throwIfAnyProductDoesNotExits(request);
        throwIfAnyInvalidQuantity(request);
        Set<String> message = new java.util.HashSet<>(Collections.emptySet());


        for (var item : request.getItems()) {
            var purchaseId = item.getProductId() + request.getUserId();
            var discountIds = discountByProductRepository.findDiscountProductId(item.getProductId(), item.getQuantity());
            var purchasingAmount = item.getQuantity();
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
                        .setMessage("Failed; Product" + item.getProductId() + " has not been purchased")
                        .setCauses("Failed to save database")
                        .setSource("ProductOrderService::orderConfirmOrThrow")
                        .toException();
            }

            subtractProductQuantityFromDBOrThrow(item.getProductId(), item.getQuantity());

            var discountWasGiven = (takenDiscountId != null);

            if (discountWasGiven) {

                var discount = discountByProductRepository.findById(takenDiscountId).orElse(null);
                if (discount != null) {
                    var childId = discount.getBonusProductId();
                    var bonusQuantity = discount.getBonusOnThreshold();
                    subtractProductQuantityFromDBOrThrow(childId, bonusQuantity);
                    message.add(String.format("You have successfully purchased product=%s of quantity=%d and got bonus=%d", item.getProductId(), purchasingAmount, bonusQuantity));

                }
            } else {
                message.add(String.format("You have successfully purchased product=%s of quantity=%d", item.getProductId(), purchasingAmount));

            }

        }
        return message.stream().toList();
    }


    //TODO Helper method

    private void throwIfAnyInvalidQuantity(OrderRequest request) throws Exception {
        for (var item : request.getItems()) {
            var productId = item.getProductId();
            var demandedAmount = item.getQuantity();
            throwIfInvalidQuantity(productId, demandedAmount);
        }

    }

    private void throwIfInvalidQuantity(String productId, int desiredPurchaseAmount) throws Exception {
        var entity = inventoryFactory.createGetInventoryUseCase().getOrThrow(productId);
        var availableAmount = entity.getQuantity();
        var productName = productFactory.createProductFinderUseCase().fetchOrThrow(productId).getName();
        var isInventoryExceed = (desiredPurchaseAmount > availableAmount);
        if (isInventoryExceed) {
            throw new ErrorMessage()
                    .setMessage("failed")
                    .setCauses(String.format("The requested quantity for product (name=%s, id=%s) exceeds the available stock of %d", productName, productId, availableAmount))
                    .setSource(this.getClass().getSimpleName())
                    .toException();

        }
        if (desiredPurchaseAmount <= 0) {
            throw new ErrorMessage()
                    .setMessage("failed")
                    .setCauses(String.format("The requested quantity for product (name=%s, id=%s) can not be less than 1", productName, productId))
                    .setSource(this.getClass().getSimpleName())
                    .toException();
        }


    }

    private int calculateDiscountAsLinear(int purchased, int minRequired, int bonusOnMin) {
        // Calculate the bonus per unit
        double bonusPerUnit = (double) bonusOnMin / minRequired;
        // Calculate the total bonus for the purchased quantity
        return (int) (bonusPerUnit * purchased);
    }

    private void throwIfUserDoesNotExit(String userId) throws Exception {
        authFactory.createUserAbsenceUseCase().throwIfNotExits(userId);
    }

    private void subtractProductQuantityFromDBOrThrow(String productId, int amountToSubtract) throws Exception {
        var rowAffected = inventoryRepository.subtractQuantity(productId, amountToSubtract);
        var isNotSuccess = !(rowAffected > 0);
        if (isNotSuccess) {
            throw new ErrorMessage()
                    .setMessage("failed")
                    .setCauses("Product with id " + productId + " +did not subtracted from databases,because no row updated in database table")
                    .setSource("ProductOrderService::subtractProductQuantityFromDBOrThrow")
                    .toException();

        }

    }

    private void throwIfAnyProductDoesNotExits(OrderRequest request) throws Exception {
        for (var requestModel : request.getItems()) {
            var productId = requestModel.getProductId();
            if (!productRepository.existsById(productId)) {
                throw new ErrorMessage()
                        .setMessage("failed")
                        .setCauses("Product with id " + productId + " does not exits")
                        .setSource("ProductOrderService::throwIfProductDoesNotExits")
                        .toException();
            }
        }

    }
}
