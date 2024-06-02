package com.kzcse.springboot.auth.data.service;

import com.kzcse.springboot.auth.data.entity.UserEntity;
import com.kzcse.springboot.auth.data.repository.UserRepository;
import com.kzcse.springboot.common.ErrorMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean doesExit(String email) {
        return userRepository.existsById(email);
    }

    public boolean doesNotExit(String email) {
        return !userRepository.existsById(email);
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
        checkUserExitsOrThrow(user.getEmail()); // throw Exception if table is  exits'
         addToDBOrThrow(user);
    }

    private void createTableIfNotPresent(UserEntity user) {
        try {
            checkUserExitsOrThrow(user.getEmail());
        } catch (Exception e) {

            System.out.println(e);
        }

    }

    private boolean addToDBOrThrow(UserEntity user) throws Exception {
        var response = userRepository.save(user);
        var isNotAdded = !(user.equals(response));
        System.out.println(response);
        if (isNotAdded) {
            throw new ErrorMessage()
                    .setMessage(user.getEmail() + " failed to add")
                    .setCauses("did not added to database")
                    .setSource("UserService::addToDBOrThrow")
                    .toException();
        }
        return true;
    }

    private void checkUserExitsOrThrow(String email) throws Exception {
        if (doesExit(email)) {
            throw new Exception("user with email " + email + " already exits");
        }

    }

}
