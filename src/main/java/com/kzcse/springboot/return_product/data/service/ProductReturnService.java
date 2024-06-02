package com.kzcse.springboot.return_product.data.service;//package com.kzcse.springboot.return_product.data.service;
//
//import com.kzcse.springboot.discount.data.repository.DiscountByProductRepository;

import com.kzcse.springboot.common.ErrorMessage;
import com.kzcse.springboot.discount.data.entity.DiscountByProductEntity;
import com.kzcse.springboot.discount.data.repository.DiscountByProductRepository;
import com.kzcse.springboot.purchase.data.entity.PurchasedProductEntity;
import com.kzcse.springboot.purchase.data.repositoy.PurchasedProductRepository;
import com.kzcse.springboot.return_product.data.entiry.ReturningPendingEntity;
import com.kzcse.springboot.return_product.data.repository.ReturnProductRepository;
import com.kzcse.springboot.return_product.domain.ProductReturnResponseModel;
import com.kzcse.springboot.return_product.domain.ReturnRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class ProductReturnService {
    private final DiscountByProductRepository discountByProductRepository;
    private final PurchasedProductRepository purchasedProductRepository;
    private final ReturnProductRepository returnProductRepository;

    public ProductReturnService(DiscountByProductRepository discountByProductRepository, PurchasedProductRepository purchasedProductRepository, ReturnProductRepository returnProductRepository) {
        this.discountByProductRepository = discountByProductRepository;
        this.purchasedProductRepository = purchasedProductRepository;
        this.returnProductRepository = returnProductRepository;
    }

    public ProductReturnResponseModel createReturnRequestOrThrow(@RequestBody ReturnRequest request) throws Exception {
        var purchasedProduct = getPurchasedHistoryOrThrow(request.getPurchaseId());
        var productId = purchasedProduct.getProductId();
        var discountId = purchasedProduct.getDiscountId();
        var wasDiscount = (discountId != null);
        if (wasDiscount) {
            return onPurchaseHadDiscountOrThrow(purchasedProduct, productId, discountId, request);
        } else {
            return onProductHadNoDiscountOrThrow(request, productId);
        }

    }

    private ProductReturnResponseModel onProductHadNoDiscountOrThrow(ReturnRequest request, String productId) throws Exception {
        saveToDBOrThrow(request.getPurchaseId(), productId, null, request.getReturnQuantity(), 0);
        return new ProductReturnResponseModel("You have to return " + request.getReturnQuantity() + " product(s)");
    }

    private ProductReturnResponseModel onPurchaseHadDiscountOrThrow(
            PurchasedProductEntity purchasedProduct,
            String productId,
            String discountId,
            ReturnRequest request
    ) throws Exception {
        var itemAfterReturn = purchasedProduct.getQuantity() - request.getReturnQuantity();
        var discount = getDiscountEntityOrThrow(discountId);


        var noMoreEligibleForDiscount = itemAfterReturn < discount.getRequiredParentQuantity();

        //TODO:If it eligible for discount then what???
        if (noMoreEligibleForDiscount) {
            var offeredProductAmount = discount.getFreeChildQuantity();
            var offeredProductId = discount.getChildId();
            saveToDBOrThrow(request.getPurchaseId(), productId, offeredProductId, request.getReturnQuantity(), offeredProductAmount);
            return new ProductReturnResponseModel(
                    "You have to return " + request.getReturnQuantity() + "+" + offeredProductAmount + "(free) product(s) "
            );
        } else {
            return new ProductReturnResponseModel(
                    "You have to return " + request.getReturnQuantity() + " product(s) ");
        }


    }

    private void saveToDBOrThrow(
            String purchasedId,
            String productId,
            String offeredProductId,
            int returningAmount,
            int offeredProductAmount
    ) throws Exception {
        var entity = new ReturningPendingEntity(purchasedId, productId, offeredProductId, returningAmount, offeredProductAmount);
        var res = returnProductRepository.save(entity);

        //make sure ReturningPendingEntity has override the equal and hashcode
        var notSaved = !(res.equals(entity));
        if (notSaved) {
            throw new ErrorMessage()
                    .setMessage("Failed")
                    .setCauses("Failed to save ReturningPendingEntity in database")
                    .setSource(this.getClass().getSimpleName() + "::saveToDBOrThrow")
                    .toException();
        }

    }

    private DiscountByProductEntity getDiscountEntityOrThrow(String discountId) throws Exception {
        var response = discountByProductRepository.findById(discountId);
        if (response.isEmpty()) {
            throw new ErrorMessage()
                    .setMessage("DiscountByProduct  with id=" + discountId + " does not exits")
                    .setCauses("Not Found in database")
                    .setSource(this.getClass().getSimpleName() + "::getPurchasedHistoryOrThrow")
                    .toException();
        }
        return response.get();

    }

    private PurchasedProductEntity getPurchasedHistoryOrThrow(String purchasedId) throws Exception {
        var response = purchasedProductRepository.findById(purchasedId);
        if (response.isEmpty()) {
            throw new ErrorMessage()
                    .setMessage("Purchase history  with id=" + purchasedId + " does not exits")
                    .setCauses("Not Found in database")
                    .setSource(this.getClass().getSimpleName() + "::getPurchasedHistoryOrThrow")
                    .toException();
        }
        return response.get();
    }
}
