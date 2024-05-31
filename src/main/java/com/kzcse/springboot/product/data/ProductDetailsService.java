package com.kzcse.springboot.product.data;

import com.kzcse.springboot.discount.data.DiscountByPriceEntity;
import com.kzcse.springboot.discount.data.DiscountByPriceRepository;
import com.kzcse.springboot.discount.data.DiscountByProductRepository;
import com.kzcse.springboot.enitity.repository.ProductRepository;
import com.kzcse.springboot.product.domain.ProductDetailsResponse;
import com.kzcse.springboot.product.domain.ProductDetailsModelBuilder;
import com.kzcse.springboot.product.domain.ProductOfferResponse;
import com.kzcse.springboot.product.domain.ProductReviewResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProductDetailsService {
    //fetch the product by id
    //fetch the offer by id
    //fetch the discount by id
    //fetch the offered product
    private final ProductRepository productRepository;
    private final DiscountByProductRepository discountByProductRepository;
    private final DiscountByPriceRepository discountByPriceRepository;

    public ProductDetailsService(ProductRepository productRepository, DiscountByProductRepository discountByProductRepository, DiscountByPriceRepository discountByPriceRepository) {
        this.productRepository = productRepository;
        this.discountByProductRepository = discountByProductRepository;
        this.discountByPriceRepository = discountByPriceRepository;
    }

    public ProductDetailsResponse fetchDetails(String productId) {
        var builder = new ProductDetailsModelBuilder(productId);
        var productResponse = productRepository.findById(productId);
        var offeredProductResponse = discountByProductRepository.findOfferedProduct(productId);
        if (productResponse.isPresent()) {
            var product = productResponse.get();
            builder
                    .setName(product.getName())
                    .setImageLink(product.getImageLink())
                    .setDescription(product.getDescription())
                    .setPrice(product.getPrice());
        }
        if (offeredProductResponse != null) {
            var offered = offeredProductResponse.stream().map(
                    offeredProduct -> {
                        var offeredProductDetailsResponse = productRepository.findById(offeredProduct.getChildId());
                        if (offeredProductDetailsResponse.isPresent()) {
                            var offeredP = offeredProductDetailsResponse.get();
                            return new ProductOfferResponse(
                                    offeredP.getName(),
                                    offeredP.getImageLink(),
                                    offeredProduct.getFreeChildQuantity(),
                                    offeredProduct.getFreeChildQuantity()
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
        return builder.build();
    }

    private DiscountByPriceEntity fetchDiscountByPrice(String productId) {
        var response = discountByPriceRepository.findById(productId);
        return response.orElse(null);

    }

    public static long getExpireTimeMs(int daysToAdd) {
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
