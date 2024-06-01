package com.kzcse.springboot.purchase.data.service;

import com.kzcse.springboot.discount.data.repository.DiscountByProductRepository;
import com.kzcse.springboot.product.data.repository.InventoryRepository;
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


    public ProductOrderService(ProductRepository productRepository, InventoryRepository inventoryRepository, DiscountByProductRepository discountByProductRepository, PurchasedProductRepository purchasedProductRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.discountByProductRepository = discountByProductRepository;
        this.purchasedProductRepository = purchasedProductRepository;
    }

    public void orderConfirm(OrderRequest request) throws Exception {
        checkProductDoesExitOrThrow(request);
        for (var item : request.getItems()) {
            var purchaseId = item.getProductId() + request.getUserId();
            var discountId = discountByProductRepository.findDiscountProductId(item.getProductId(), item.getQuantity());

            var expireDate = LocalDate.now();
            var entity = new PurchasedProductEntity(purchaseId, request.getUserId(), item.getProductId(), item.getQuantity(), discountId, expireDate);

            var response = purchasedProductRepository.save(entity);
            var isSuccess = (entity.equals(response));
            if (!isSuccess) {
                throw new Exception("Product " + item.getProductId() + " has not been purchased\nBecause failed to save database");
            }

            subtractProductQuantityFromDBOrThrow(item.getProductId(), item.getQuantity());
            //inventoryRepository.subtractQuantity(item.getProductId(), item.getQuantity());
            if (discountId != null) {
                var discount = discountByProductRepository.findById(discountId).orElse(null);
                if (discount != null) {
                    var childId = discount.getChildId();
                    var quantity = discount.getFreeChildQuantity();
                    subtractProductQuantityFromDBOrThrow(childId, quantity);
                   // inventoryRepository.subtractQuantity(childId, quantity);
                }
                //System.out.println(entity);
            }

        }
    }
    private void subtractProductQuantityFromDBOrThrow(String productId, int amountToSubtract) throws Exception{
       var res= inventoryRepository.subtractQuantity(productId,amountToSubtract);
       var isNotSuccess=!(res==amountToSubtract);
       if (isNotSuccess)
           throw new Exception("Product with id " +productId+
                   " did not subtracted from database\nServerError::DiscountByPriceService::addDiscountByPrice");

    }

    private void checkProductDoesExitOrThrow(OrderRequest request) throws Exception {
        for (var requestModel : request.getItems()) {
            if (!productRepository.existsById(requestModel.getProductId())) {
                throw new Exception("Product with id " + requestModel.getProductId() +
                        " does not exits\nServerError::DiscountByPriceService::addDiscountByPrice");
            }
        }

    }
}
