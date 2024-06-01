package com.kzcse.springboot.product.data.service;

import com.kzcse.springboot.contract.APIResponseDecorator;
import com.kzcse.springboot.discount.data.entity.DiscountByPriceEntity;
import com.kzcse.springboot.discount.data.repository.DiscountByPriceRepository;
import com.kzcse.springboot.discount.data.repository.DiscountByProductRepository;
import com.kzcse.springboot.product.data.ProductEntity;
import com.kzcse.springboot.product.data.ProductRepository;
import com.kzcse.springboot.product.domain.model.response_model.ProductDetailsResponse;
import com.kzcse.springboot.product.domain.ProductDetailsModelBuilder;
import com.kzcse.springboot.product.domain.model.response_model.ProductOfferResponse;
import com.kzcse.springboot.product.domain.model.response_model.ProductReviewResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
/**
 * <ul>
 *   <li>Responsible for generating a product details</li>
 *   <li>Details contain product basic info,offer by price,offer by product,reviews</li>
 * </ul>
 */
@Service
public class ProductDetailsService {

    private final ProductRepository productRepository;
    private final DiscountByProductRepository discountByProductRepository;
    private final DiscountByPriceRepository discountByPriceRepository;

    public ProductDetailsService(ProductRepository productRepository, DiscountByProductRepository discountByProductRepository, DiscountByPriceRepository discountByPriceRepository) {
        this.productRepository = productRepository;
        this.discountByProductRepository = discountByProductRepository;
        this.discountByPriceRepository = discountByPriceRepository;

    }

    /**
     * <ul>
     *   <li>Fetch the @{@link ProductEntity} that contain the product info</li>
     *   <li>Fetch OfferByProduct info(if available) </li>
     *   <li>Fetch OfferByPrice info(if available) </li>
     *   <li>{@link ProductDetailsResponse} will directly convert to JSON in client side</li>
     * </ul>
     */
    public APIResponseDecorator<ProductDetailsResponse> fetchDetails(String productId) {
        var builder = new ProductDetailsModelBuilder(productId);
        var productResponse = productRepository.findById(productId);
        if (productResponse.isEmpty()){
            return new APIResponseDecorator<ProductDetailsResponse>()
                    .onFailure("Server Error:ProductDetailsService::fetchDetails():Product is not found or failed to read,check the product id");
        }

        var offeredProductResponse = discountByProductRepository.findOfferedProduct(productId);
        var product = productResponse.get();
        builder
                .setName(product.getName())
                .setImageLink(product.getImageLink())
                .setDescription(product.getDescription())
                .setPrice(product.getPrice());
        if (offeredProductResponse != null) {
            var offered = offeredProductResponse.stream().map(
                    offer -> {
                        var offeredProductDetailsResponse = productRepository.findById(offer.getChildId());
                        if (offeredProductDetailsResponse.isPresent()) {
                            var offeredProduct = offeredProductDetailsResponse.get();
                            return new ProductOfferResponse(
                                    offeredProduct.getName(),
                                    offeredProduct.getImageLink(),
                                    offer.getRequiredParentQuantity(),
                                    offer.getFreeChildQuantity(),
                                    offer.getExpirationTimeInMs()
                            );
                        } else
                            return null;
                    });

            offered.findFirst().ifPresent(builder::setOfferedProduct);
            createDummyReview().forEach(builder::addReview);

        }
        var discountByPrice = fetchDiscountByPrice(productId);
        if (discountByPrice != null) {
            builder.setDiscount(discountByPrice.getAmount(), getExpireTimeMs(3));
        }
        return new APIResponseDecorator<ProductDetailsResponse>().onSuccess(builder.build());
    }

    private DiscountByPriceEntity fetchDiscountByPrice(String productId) {
        var response = discountByPriceRepository.findById(productId);
        return response.orElse(null);

    }

    private long getExpireTimeMs(int daysToAdd) {
        long currentTimeInMs = System.currentTimeMillis();
        long msInADay = TimeUnit.DAYS.toMillis(1);
        long addedTimeInMs = daysToAdd * msInADay;
        return currentTimeInMs + addedTimeInMs;
    }

    private List<ProductReviewResponse> createDummyReview() {
        return List.of(
                new ProductReviewResponse(
                        "John Doe",
                        "Great performance for the price!",
                        List.of("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ9DIujrFio7TaE_ZQv_1K0eNZN1y0lErozyHrhPrwMEA&s")
                ),
                new ProductReviewResponse(
                        "Jane Smith",
                        "Battery life could be better, but overall a good buy.",
                        List.of("https://diamu.com.bd/wp-content/uploads/2020/01/Apple-Iphone-11-White.jpg")
                ));
    }
}
