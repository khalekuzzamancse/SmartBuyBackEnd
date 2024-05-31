package com.kzcse.springboot.product.domain;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsModelBuilder {
    private String productId;
    private String name;
    private List<String> imagesLink = new ArrayList<>();
    private String description;
    private String originalPrice;
    private String priceDiscount = "0";  // Default to "0" if not set
    private String priceOnDiscount;
    private ProductOfferModel offeredProduct;
    private final List<ProductReviewModel> reviews = new ArrayList<>();

    public ProductDetailsModelBuilder(String productId){
        this.productId=productId;
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
        this.originalPrice = String.valueOf(price);
        this.priceOnDiscount = this.originalPrice;
        return this;
    }

    public ProductDetailsModelBuilder setDiscount(int discount) {
        this.priceDiscount = String.valueOf(discount);
        if (this.originalPrice != null) {
            int originalPriceInt = Integer.parseInt(this.originalPrice);
            int priceOnDiscountInt = originalPriceInt - discount;
            this.priceOnDiscount = String.valueOf(priceOnDiscountInt);
        }
        return this;
    }

    public ProductDetailsModelBuilder setOfferedProduct(ProductOfferModel offeredProduct) {
        this.offeredProduct = offeredProduct;
        return this;
    }

    public ProductDetailsModelBuilder addReview(ProductReviewModel review) {
        if (review != null) {
            this.reviews.add(review);
        }
        return this;
    }

    public ProductDetailsModelBuilder addReviews(List<ProductReviewModel> reviews) {
        if (reviews != null) {
            this.reviews.addAll(reviews);
        }
        return this;
    }

    public ProductDetailsModel build() {
        ProductDetailsModel productDetailsModel = new ProductDetailsModel();
        productDetailsModel.setProductId(productId);
        productDetailsModel.setName(this.name);
        productDetailsModel.setImagesLink(this.imagesLink);
        productDetailsModel.setDescription(this.description);
        productDetailsModel.setOriginalPrice(this.originalPrice);
        productDetailsModel.setPriceDiscount(this.priceDiscount);
        productDetailsModel.setPriceOnDiscount(this.priceOnDiscount);
        productDetailsModel.setOfferedProduct(this.offeredProduct);
        productDetailsModel.setReviews(this.reviews);
        return productDetailsModel;
    }

    public static ProductDetailsModel getDemoModel() {
        return new ProductDetailsModelBuilder("01")
                .setName("Nokia C32")
                .setImageLink("https://d61s2hjse0ytn.cloudfront.net/card_image/None/blackcard.webp")
                .setDescription("Stylish design with a 6.5-inch HD+ display, dual-camera with 50MP primary lens, powered by Unisoc SC9863A1 CPU.")
                .setPrice(12999)
                .setDiscount(999)
                .setOfferedProduct(new ProductOfferModel(
                        "Limited Time Offer", // Assuming a product name for the offer
                        "https://d61s2hjse0ytn.cloudfront.net/card_image/None/Nokia_150_2023_Cart.webp", // Assuming a URL for the offer image
                        "Buy 1 Get 1 Free", // Required quantity for the offer
                        "1" // Free quantity
                ))
                .addReview(new ProductReviewModel(
                        "John Doe",
                        "Great performance for the price!",
                        List.of("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ9DIujrFio7TaE_ZQv_1K0eNZN1y0lErozyHrhPrwMEA&s") // Assuming URLs for review images
                ))
                .addReview(new ProductReviewModel(
                        "Jane Smith",
                        "Battery life could be better, but overall a good buy.",
                        List.of("https://diamu.com.bd/wp-content/uploads/2020/01/Apple-Iphone-11-White.jpg")
                ))
                .build();

    }
}
