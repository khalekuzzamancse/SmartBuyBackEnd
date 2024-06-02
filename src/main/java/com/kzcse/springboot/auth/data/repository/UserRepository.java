package com.kzcse.springboot.auth.data.repository;

import com.kzcse.springboot.auth.data.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, String> {
}
