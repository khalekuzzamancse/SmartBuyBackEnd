package com.kzcse.springboot.auth.domain.usecase;

//Used by other service that is why centering to avoid duplicate

import com.kzcse.springboot.auth.data.repository.UserRepository;
import com.kzcse.springboot.common.ErrorMessage;


public class UserExistenceUseCase {
    private final UserRepository repository;


    public UserExistenceUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public void throwOnUserExit(String email) throws Exception {
        var doesExits = repository.existsById(email);
        if (doesExits) {
            throw new ErrorMessage()
                    .setMessage("user with email=" + email + " already exits")
                    .setCauses("Found in database")
                    .setSource(this.getClass().getSimpleName())
                    .toException();
        }

    }
}
