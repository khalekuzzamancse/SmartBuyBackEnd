package com.kzcse.springboot.product.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductReviewModel {
    private String reviewerName;
    private String comment;
    private List<String> imagesLink;
}
