package com.kzcse.springboot.enitity.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public record ReturningPendingItem(
        @Id String id,
        int returnedParentQuantity,
        int returnedChildQuantity,
        LocalDateTime returnRequestDate
) {
}
