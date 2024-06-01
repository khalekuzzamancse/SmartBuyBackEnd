package com.kzcse.springboot.contract;

import lombok.Getter;
import lombok.Setter;

/**
 * If any error occurs than data is null
 * If no error occurs or success then error message=null
 *
 * @param <T>
 */
@Getter
@Setter
public class APIResponseDecorator<T> {
    private T response;
    private String errorMessage;
    private boolean success;

    public APIResponseDecorator<T> onSuccess(T response) {
        this.response = response;
        this.errorMessage = null;
        this.success =true;
        return this;
    }

    public APIResponseDecorator<T> onFailure(String errorMessage) {
        this.response = null;
        this.errorMessage = errorMessage;
        this.success =false;
        return this;
    }
}
