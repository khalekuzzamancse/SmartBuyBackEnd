package com.kzcse.springboot.purchase.domain.usecase;

import com.kzcse.springboot.discount.domain.DiscountFactory;
import com.kzcse.springboot.product.domain.ProductFactory;
import com.kzcse.springboot.purchase.domain.request_model.OrderRequest;

public class PurchaseBillGenerateUserCase {
    private final ProductFactory productFactory;
    private final DiscountFactory discountFactory;

    public PurchaseBillGenerateUserCase(ProductFactory productFactory, DiscountFactory discountFactory) {
        this.productFactory = productFactory;
        this.discountFactory = discountFactory;
    }

    public int generateBillOrThrow(OrderRequest request) throws Exception {
        //TODO:Check inventory
        var total = 0;
        for (var product : request.getItems()) {
            var productId = product.getProductId();
            var quantity = product.getQuantity();

            var price = getPriceOrThrow(productId) * quantity;
            var discount = getDiscountOrZero(productId);

            total = total + (price - discount);
        }

        return total;
    }


    private int getPriceOrThrow(String productId) throws Exception {
        return productFactory.createProductFinderUseCase().fetchOrThrow(productId).getPrice();

    }

    private int getDiscountOrZero(String productId) throws Exception {
        try {
            var entity = discountFactory.createFindDiscountByPriceUserCase().findOrThrow(productId);
            return entity.getAmount();
        } catch (Exception e) {
            return 0;
        }

    }
}
