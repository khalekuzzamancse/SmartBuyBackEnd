package com.kzcse.springboot;

import com.kzcse.springboot.product.domain.ProductDetailsModel;
import com.kzcse.springboot.product.domain.ProductDetailsModelBuilder;
import com.kzcse.springboot.product.domain.ProductOfferModel;
import com.kzcse.springboot.product.domain.ProductReviewModel;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDetailsModelTest {

    @Test
    public void testProductDetailsModelBuilder() {
        // Arrange
        String name = "Test Product";
        List<String> images = Arrays.asList("image1.jpg", "image2.jpg");
        String description = "This is a test product.";
        int price = 100;
        int discount = 20;
        ProductOfferModel offer = new ProductOfferModel("Test Offer", "offerImage.jpg", "2", "1");
        ProductReviewModel review1 = new ProductReviewModel("John Doe", "Great product!", Collections.singletonList("reviewImage1.jpg"));
        ProductReviewModel review2 = new ProductReviewModel("Jane Doe", "Not bad.", Collections.singletonList("reviewImage2.jpg"));

        // Act
        ProductDetailsModel product = new ProductDetailsModelBuilder("01")
                .setName(name)
                .setImagesLink(images)
                .setDescription(description)
                .setPrice(price)
                .setDiscount(discount)
                .setOfferedProduct(offer)
                .addReview(review1)
                .addReview(review2)
                .build();

        // Assert
        assertEquals(name, product.getName());
        assertEquals(images, product.getImagesLink());
        assertEquals(description, product.getDescription());
        assertEquals(String.valueOf(price), product.getOriginalPrice());
        assertEquals(String.valueOf(discount), product.getPriceDiscount());
        assertEquals(String.valueOf(price - discount), product.getPriceOnDiscount());
        assertEquals(offer, product.getOfferedProduct());
        assertEquals(2, product.getReviews().size());
        assertEquals(review1, product.getReviews().get(0));
        assertEquals(review2, product.getReviews().get(1));
    }

    @Test
    public void testProductDetailsModelBuilderSingleImageLink() {
        // Arrange
        String name = "Test Product Single Image";
        String image = "singleImage.jpg";
        String description = "This is a test product with a single image.";
        int price = 200;
        int discount = 50;
        ProductOfferModel offer = new ProductOfferModel("Test Offer", "offerImage.jpg", "2", "1");

        // Act
        ProductDetailsModel product = new ProductDetailsModelBuilder("01")
                .setName(name)
                .setImageLink(image)
                .setDescription(description)
                .setPrice(price)
                .setDiscount(discount)
                .setOfferedProduct(offer)
                .build();

        // Assert
        assertEquals(name, product.getName());
        assertEquals(Collections.singletonList(image), product.getImagesLink());
        assertEquals(description, product.getDescription());
        assertEquals(String.valueOf(price), product.getOriginalPrice());
        assertEquals(String.valueOf(discount), product.getPriceDiscount());
        assertEquals(String.valueOf(price - discount), product.getPriceOnDiscount());
        assertEquals(offer, product.getOfferedProduct());
        assertTrue(product.getReviews().isEmpty());
    }

    @Test
    public void testProductDetailsModelBuilderNoDiscount() {
        // Arrange
        String name = "Test Product No Discount";
        List<String> images = Arrays.asList("image1.jpg", "image2.jpg");
        String description = "This is a test product with no discount.";
        int price = 300;
        ProductOfferModel offer = new ProductOfferModel("Test Offer", "offerImage.jpg", "2", "1");

        // Act
        ProductDetailsModel product = new ProductDetailsModelBuilder("01")
                .setName(name)
                .setImagesLink(images)
                .setDescription(description)
                .setPrice(price)
                .setOfferedProduct(offer)
                .build();

        // Assert
        assertEquals(name, product.getName());
        assertEquals(images, product.getImagesLink());
        assertEquals(description, product.getDescription());
        assertEquals(String.valueOf(price), product.getOriginalPrice());
        assertEquals("0", product.getPriceDiscount());
        assertEquals(String.valueOf(price), product.getPriceOnDiscount());
        assertEquals(offer, product.getOfferedProduct());
        assertTrue(product.getReviews().isEmpty());
    }
}
