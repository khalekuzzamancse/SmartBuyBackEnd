package com.kzcse.springboot.purchase.data.service;

import com.kzcse.springboot.auth.data.service.UserService;
import com.kzcse.springboot.auth.domain.AuthFactory;
import com.kzcse.springboot.common.ErrorMessage;
import com.kzcse.springboot.discount.data.entity.DiscountByProductEntity;
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


        for (var product : request.getItems()) {
            var purchaseId = product.getProductId() + request.getUserId();
            var productId=product.getProductId();
            var discountIds = discountByProductRepository.findDiscountProductId(product.getProductId(), product.getQuantity());
            var purchasingAmount = product.getQuantity();
            String takenDiscountId = null;
            if (!discountIds.isEmpty()) {
                takenDiscountId = discountIds.get(discountIds.size() - 1); //taken the last once
            }



            var eligibleForDiscount = (takenDiscountId != null);

            if (eligibleForDiscount) {
                var discount = discountByProductRepository.findById(takenDiscountId).orElse(null);
                if (discount != null) {
                    var bonusProductId = discount.getBonusProductId();
                    var bonusQuantity = calculateBonusQuantity(purchasingAmount,discount);
                    //TODO:It is better to save database after all valid check
                    throwIfInvalidQuantity(bonusProductId,bonusQuantity);

                    subtractProductQuantityFromDBOrThrow(bonusProductId, bonusQuantity);
                    message.add(String.format("You have successfully purchased product=%s of quantity=%d and got bonus=%d", product.getProductId(), purchasingAmount, bonusQuantity));

                }
            } else {
                message.add(String.format("You have successfully purchased product=%s of quantity=%d", product.getProductId(), purchasingAmount));

            }

            //TODO:If eligible for offer,then update database for  offer or throw ,then update product,otherwise will cause inconsistent
            subtractProductQuantityFromDBOrThrow(productId, purchasingAmount);
            //Save the history
            var entity = new PurchasedProductEntity(purchaseId, request.getUserId(),productId, purchasingAmount, takenDiscountId,  LocalDate.now());
            savePurchaseHistoryOrThrow(entity);
        }

        return message.stream().toList();
    }
    private void savePurchaseHistoryOrThrow(PurchasedProductEntity entity) throws Exception{
        var response = purchasedProductRepository.save(entity);
        var isSuccess = (entity.equals(response));
        if (!isSuccess) {
            throw new ErrorMessage()
                    .setMessage("Failed; Product" + entity.getProductId() + " has not been purchased")
                    .setCauses("Failed to save database")
                    .setSource(this.getClass().getSimpleName()+"::orderConfirmOrThrow")
                    .toException();
        }
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
        inventoryFactory.createValidator().validateOrThrow(productId, desiredPurchaseAmount);

    }

    private int calculateBonusQuantity(int purchased, DiscountByProductEntity discount) {
        var minRequired = discount.getBonusEligibilityThreshold();
        var bonusOnMin = discount.getBonusOnThreshold();
        if (discount.isConstant())
            return bonusOnMin;

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
