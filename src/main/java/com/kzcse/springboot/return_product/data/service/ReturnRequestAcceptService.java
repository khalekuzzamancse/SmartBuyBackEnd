package com.kzcse.springboot.return_product.data.service;//package com.kzcse.springboot.return_product.data.service;
//
//import com.kzcse.springboot.discount.data.repository.DiscountByProductRepository;

import com.kzcse.springboot.common.ErrorMessage;
import com.kzcse.springboot.discount.data.repository.DiscountByProductRepository;
import com.kzcse.springboot.inventory.domain.InventoryFactory;
import com.kzcse.springboot.purchase.domain.PurchasedFactory;
import com.kzcse.springboot.return_product.data.entiry.PendingReturnProduct;
import com.kzcse.springboot.return_product.data.repository.ReturnProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ReturnRequestAcceptService {
    private final ReturnProductRepository returnProductRepository;
    private final PurchasedFactory purchasedFactory;
    private final InventoryFactory inventoryFactory;
    private final DiscountByProductRepository discountByProductRepository;

    public ReturnRequestAcceptService(ReturnProductRepository returnProductRepository, PurchasedFactory purchasedFactory, InventoryFactory inventoryFactory, DiscountByProductRepository discountByProductRepository) {
        this.returnProductRepository = returnProductRepository;
        this.purchasedFactory = purchasedFactory;
        this.inventoryFactory = inventoryFactory;
        this.discountByProductRepository = discountByProductRepository;
    }

    public void acceptRequestOrThrow(String purchaseId) throws Exception {
        var entity = getPendingEntityOrThrow(purchaseId);

        var mainProductId = entity.getPurchasedProductId();
        var bonusProductId = entity.getBonusProductId(); //TODO:Can be NULL,Handle it
        var returnAmountOfMainProduct = entity.getMainProductReturnQuantity();
        var returnAmountOfBonusProduct = entity.getBonusReturnQuantity(); ////TODO:Can be NULL/0,Handle it


        //add main product return amount to DB
        addToDBOrThrow(mainProductId, returnAmountOfMainProduct);
        //add bonus product return amount to DB
        addToDBOrThrow(bonusProductId, returnAmountOfBonusProduct);

        //update the purchase history
        updatePurchaseHistory(purchaseId, mainProductId, returnAmountOfMainProduct);
        //remove from pending
        removeFromPending(entity);

    }


    private void addToDBOrThrow(String productId, int returningAmount) throws Exception {
        inventoryFactory.createAddToExitingInventoryUseCase().addOrThrow(productId, returningAmount);
    }


    private PendingReturnProduct getPendingEntityOrThrow(String purchasedId) throws Exception {
        var response = returnProductRepository.findById(purchasedId);
        if (response.isEmpty()) {
            throw new ErrorMessage()
                    .setMessage("purchasedId  with id=" + purchasedId + " does not exits")
                    .setCauses("Not Found in database")
                    .setSource(this.getClass().getSimpleName() + "::getPendingEntityOrThrow")
                    .toException();

        }
        return response.get();
    }

    private void updatePurchaseHistory(String purchaseId, String mainProductId, int mainProductReturnAmount) throws Exception {
        var purchasedQuantity = purchasedFactory.createHistoryExistenceUseCase().getOrThrow(purchaseId).getQuantity();
        var remainingQuantityAfterReturn = purchasedQuantity - mainProductReturnAmount;
        var removeDiscount = discountByProductRepository.findDiscountProductId(mainProductId, remainingQuantityAfterReturn).isEmpty();//discountWillNotAvailableAfterReturn
        if (remainingQuantityAfterReturn>=0){
            purchasedFactory.purchaseHistoryUpdaterUseCase().updateOrThrow(purchaseId, remainingQuantityAfterReturn, removeDiscount);
        }

    }
    private void removeFromPending(PendingReturnProduct entity){
        returnProductRepository.delete(entity);

    }
}
