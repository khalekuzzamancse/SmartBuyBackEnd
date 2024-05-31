package com.kzcse.springboot.product.domain;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsModelBuilder {
    private final String productId;
    private String name;
    private List<String> imagesLink = new ArrayList<>();
    private String description;
    private int originalPrice;
    private DiscountByPriceResponse discountByPrice;
    private ProductOfferResponse offeredProduct;
    private final List<ProductReviewResponse> reviews = new ArrayList<>();

    public ProductDetailsModelBuilder(String productId) {
        this.productId = productId;
    }

    public ProductDetailsModelBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ProductDetailsModelBuilder setImagesLink(List<String> imagesLink) {
        this.imagesLink = new ArrayList<>(imagesLink);
        return this;
    }

    public ProductDetailsModelBuilder setImageLink(String imageLink) {
        this.imagesLink.add(imageLink);
        return this;
    }

    public ProductDetailsModelBuilder setDescription(String description) {
        this.description = description;
        return this;
    }


    public ProductDetailsModelBuilder setPrice(int price) {
        this.originalPrice = price;
        return this;
    }

    public ProductDetailsModelBuilder setDiscount(int amount, Long expireTimeMs) {
        this.discountByPrice = new DiscountByPriceResponse(amount, expireTimeMs);
        return this;
    }



    public ProductDetailsModelBuilder setOfferedProduct(ProductOfferResponse offeredProduct) {
        this.offeredProduct = offeredProduct;
        return this;
    }

    public ProductDetailsModelBuilder addReview(ProductReviewResponse review) {
        if (review != null) {
            this.reviews.add(review);
        }
        return this;
    }

    public ProductDetailsModelBuilder addReviews(List<ProductReviewResponse> reviews) {
        if (reviews != null) {
            this.reviews.addAll(reviews);
        }
        return this;
    }

    public ProductDetailsResponse build() {
        ProductDetailsResponse productDetailsResponse = new ProductDetailsResponse();
        productDetailsResponse.setProductId(productId);
        productDetailsResponse.setName(this.name);
        productDetailsResponse.setImagesLink(this.imagesLink);
        productDetailsResponse.setDescription(this.description);
        productDetailsResponse.setPrice(this.originalPrice);
        productDetailsResponse.setDiscountByPrice(discountByPrice);
        productDetailsResponse.setDiscountByProduct(this.offeredProduct);
        productDetailsResponse.setReviews(this.reviews);
        return productDetailsResponse;
    }

    public static ProductDetailsResponse getDemoModel() {
        return new ProductDetailsModelBuilder("01")
                .setName("Nokia C32")
                .setImageLink("https://d61s2hjse0ytn.cloudfront.net/card_image/None/blackcard.webp")
                .setDescription("Stylish design with a 6.5-inch HD+ display, dual-camera with 50MP primary lens, powered by Unison SC9863A1 CPU.")
                .setPrice(12999)
                .setDiscount(999,System.currentTimeMillis())
                .setOfferedProduct(new ProductOfferResponse(
                        "Iphone 5", // Assuming a product name for the offer
                        "https://d61s2hjse0ytn.cloudfront.net/card_image/None/Nokia_150_2023_Cart.webp", // Assuming a URL for the offer image
                        2, // Required quantity for the offer
                        1 // Free quantity
                ))
                .addReview(new ProductReviewResponse(
                        "John Doe",
                        "Great performance for the price!",
                        List.of("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ9DIujrFio7TaE_ZQv_1K0eNZN1y0lErozyHrhPrwMEA&s") // Assuming URLs for review images
                ))
                .addReview(new ProductReviewResponse(
                        "Jane Smith",
                        "Battery life could be better, but overall a good buy.",
                        List.of("https://diamu.com.bd/wp-content/uploads/2020/01/Apple-Iphone-11-White.jpg")
                ))
                .build();

    }
}
