package com.kzcse.springboot.return_product.data.service;//package com.kzcse.springboot.return_product.data.service;
//
//import com.kzcse.springboot.discount.data.repository.DiscountByProductRepository;

import com.kzcse.springboot.common.ErrorMessage;
import com.kzcse.springboot.inventory.domain.InventoryFactory;
import com.kzcse.springboot.return_product.data.entiry.PendingReturnProduct;
import com.kzcse.springboot.return_product.data.repository.ReturnProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ReturnRequestAcceptService {
    private final ReturnProductRepository returnProductRepository;
    private final InventoryFactory inventoryFactory;

    public ReturnRequestAcceptService(ReturnProductRepository returnProductRepository, InventoryFactory inventoryFactory) {
        this.returnProductRepository = returnProductRepository;
        this.inventoryFactory = inventoryFactory;
    }

    public void acceptRequestOrThrow(String purchaseId) throws Exception {
        var entity = getPendingEntityOrThrow(purchaseId);
        var mainProductId=entity.getPurchasedProductId();
        var bonusProductId=entity.getBonusProductId(); //TODO:Can be NULL,Handle it
        var returnAmountOfMainProduct=entity.getMainProductReturnQuantity();
        var returnAmountOfBonusProduct=entity.getBonusReturnQuantity(); ////TODO:Can be NULL/0,Handle it


        //add main product return amount to DB
        addToDBOrThrow(mainProductId, returnAmountOfMainProduct);
        //add bonus product return amount to DB
        addToDBOrThrow(bonusProductId, returnAmountOfBonusProduct);

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
}
