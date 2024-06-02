package com.kzcse.springboot.auth.domain;

import com.kzcse.springboot.auth.data.repository.UserRepository;
import com.kzcse.springboot.auth.domain.usecase.UserAbsentUseCase;
import com.kzcse.springboot.auth.domain.usecase.UserExistenceUseCase;
import org.springframework.stereotype.Component;

@Component
public  class AuthFactory {
    private final UserRepository repository;

    public AuthFactory(UserRepository repository) {
        this.repository = repository;
    }

    public UserExistenceUseCase createUserExistenceUseCase(){
        return  new UserExistenceUseCase(repository);
    }

    public UserAbsentUseCase createUserAbsenceUseCase(){
        return  new UserAbsentUseCase(repository);
    }


}
