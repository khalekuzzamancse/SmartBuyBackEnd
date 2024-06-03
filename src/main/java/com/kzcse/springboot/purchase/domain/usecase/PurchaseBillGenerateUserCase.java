package com.kzcse.springboot.purchase.domain.usecase;

import com.kzcse.springboot.discount.domain.DiscountFactory;
import com.kzcse.springboot.inventory.domain.InventoryFactory;
import com.kzcse.springboot.product.data.entity.ProductEntity;
import com.kzcse.springboot.product.domain.ProductFactory;
import com.kzcse.springboot.purchase.domain.request_model.OrderRequest;
import com.kzcse.springboot.purchase.domain.response_model.OrderBillResponse;
import com.kzcse.springboot.purchase.domain.response_model.OrderResponse;

import java.util.HashSet;
import java.util.Set;

public class PurchaseBillGenerateUserCase {
    private final ProductFactory productFactory;
    private final DiscountFactory discountFactory;
    private final InventoryFactory inventoryFactory;

    public PurchaseBillGenerateUserCase(ProductFactory productFactory, DiscountFactory discountFactory, InventoryFactory inventoryFactory) {
        this.productFactory = productFactory;
        this.discountFactory = discountFactory;
        this.inventoryFactory = inventoryFactory;
    }

    public OrderBillResponse generateBillOrThrow(OrderRequest request) throws Exception {

        //To avoid multiple copy of same item
        Set<OrderResponse> orderResponses = new HashSet<>();
        var total = 0;
        for (var product : request.getItems()) {
            var id = product.getProductId();
            var quantity = product.getQuantity();
            validateInventoryOrThrow(id, quantity);

            var entity = getProductOrThrow(id);

            var name = entity.getName();
            var unitPrice = entity.getPrice();
            var discount = getDiscountOrZero(id);

            var originalPrice = unitPrice * quantity;
            var discountedPrice = originalPrice - discount;
            orderResponses.add(new OrderResponse(id, name, unitPrice, quantity, discount, originalPrice, discountedPrice));
            total = total + (originalPrice - discount);
        }

        return new OrderBillResponse(orderResponses.stream().toList(), total);
    }


    private ProductEntity getProductOrThrow(String productId) throws Exception {
        return productFactory.createProductFinderUseCase().fetchOrThrow(productId);

    }

    private int getDiscountOrZero(String productId) throws Exception {
        try {
            var entity = discountFactory.createFindDiscountByPriceUserCase().findOrThrow(productId);
            return entity.getAmount();
        } catch (Exception e) {
            return 0;
        }

    }

    private void validateInventoryOrThrow(String productId, int desiredPurchaseAmount) throws Exception {
        inventoryFactory.createValidator().validateOrThrow(productId, desiredPurchaseAmount);
    }
}
