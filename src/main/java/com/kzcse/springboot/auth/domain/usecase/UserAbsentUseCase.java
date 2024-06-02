package com.kzcse.springboot.auth.domain.usecase;

//Used by other service that is why centering to avoid duplicate

import com.kzcse.springboot.auth.data.repository.UserRepository;
import com.kzcse.springboot.common.ErrorMessage;

public class UserAbsentUseCase {
    private final UserRepository repository;


    public UserAbsentUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public void throwIfNotExits(String email) throws Exception {
        var doesNotExits = !(repository.existsById(email));
        if (doesNotExits) {
            throw new ErrorMessage()
                    .setMessage("user with email=" + email + " not exits")
                    .setCauses("Not Found in database")
                    .setSource(this.getClass().getSimpleName())
                    .toException();
        }

    }
}
