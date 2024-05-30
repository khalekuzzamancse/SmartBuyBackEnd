package com.kzcse.springboot.enitity.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
@Entity
public record User(
        @Id String userId,
        String username,
        String email,
        String password,
        String address,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}