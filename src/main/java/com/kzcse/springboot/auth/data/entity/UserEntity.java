package com.kzcse.springboot.auth.data.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "User")
@Entity
public class UserEntity {
    @Id
    private String email;
    private String username;
    private String password;
}