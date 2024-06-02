package com.kzcse.springboot.auth.data.service;

import com.kzcse.springboot.auth.data.entity.UserEntity;
import com.kzcse.springboot.auth.data.repository.UserRepository;
import com.kzcse.springboot.auth.domain.AuthFactory;
import com.kzcse.springboot.auth.domain.usecase.UserExistenceUseCase;
import com.kzcse.springboot.common.ErrorMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthFactory factory;

    public UserService(UserRepository userRepository, AuthFactory factory) {
        this.userRepository = userRepository;
        this.factory = factory;
    }


    public List<UserEntity> getAllUser() {
        return
                StreamSupport
                        .stream(userRepository
                                .findAll()
                                .spliterator(), false
                        )
                        .toList();

    }

    public void addUserOrThrow(UserEntity user) throws Exception {
        // createTableIfNotPresent(user);
        throwIfUserExits(user.getEmail());
        addToDBOrThrow(user);
    }
    private void throwIfUserExits(String email) throws  Exception{
        factory.createUserExistenceUseCase().throwOnUserExit(email);
    }

    private void createTableIfNotPresent(UserEntity user) {
        try {
            new UserExistenceUseCase(userRepository).throwOnUserExit(user.getEmail());
        } catch (Exception e) {

            System.out.println(e);
        }

    }

    private void addToDBOrThrow(UserEntity user) throws Exception {
        var response = userRepository.save(user);
        var isNotAdded = !(user.equals(response));
        System.out.println(response);
        if (isNotAdded) {
            throw new ErrorMessage()
                    .setMessage(user.getEmail() + " failed to add")
                    .setCauses("did not added to database")
                    .setSource(this.getClass().getSimpleName() + "::addToDBOrThrow")
                    .toException();
        }
    }


}
