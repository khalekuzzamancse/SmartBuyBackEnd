package com.kzcse.springboot.auth.api;

import com.kzcse.springboot.auth.data.entity.UserEntity;
import com.kzcse.springboot.auth.data.service.UserService;
import com.kzcse.springboot.common.APIResponseDecorator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/auth/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Error handling
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(NoSuchElementException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<UserEntity>> getAllUser() {
        try {
            return ResponseEntity.ok(userService.getAllUser());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED) // response code for success
    public APIResponseDecorator<String> addUser(@RequestBody UserEntity user) {
        try {
            userService.addUserOrThrow(user);
            return new APIResponseDecorator<String>().onSuccess("added successfully");

        } catch (Exception e) {

            return new APIResponseDecorator<String>().withException(e, "failed", "UserController::addUser");
        }
    }
}

