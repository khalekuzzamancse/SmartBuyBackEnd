package com.kzcse.springboot.common;

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
    private ErrorMessage message;
    private boolean success;

    public APIResponseDecorator<T> onSuccess(T response) {
        this.response = response;
        this.message = null;
        this.success = true;
        return this;
    }

    public APIResponseDecorator<T> onFailure(ErrorMessage errorMessage) {
        this.response = null;
        this.message = errorMessage;
        this.success = false;
        return this;
    }

    public APIResponseDecorator<T> withException(Exception e, String message, String source) {
        ErrorMessage errorMessage;
        if (e instanceof ErrorMessageException) {
            errorMessage = ((ErrorMessageException) e).get();

        } else {
            errorMessage = new ErrorMessage()
                    .setMessage(message)
                    .setCauses(e.getMessage())
                    .setSource(source);
        }
        return new APIResponseDecorator<T>().onFailure(errorMessage);
    }

}
