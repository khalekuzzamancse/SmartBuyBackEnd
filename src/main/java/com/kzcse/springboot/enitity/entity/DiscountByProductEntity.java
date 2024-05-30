package com.kzcse.springboot.enitity.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DiscountByProductEntity {
    @Id
    private String id;
    private String parentId;
    private String childId;
    private int requiredParentQuantity;
    private int freeChildQuantity;

}
