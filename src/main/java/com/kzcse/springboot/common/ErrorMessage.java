package com.kzcse.springboot.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErrorMessage {
    private  String message;
    private  String causes="Unknown";
    private  String source="Unknown";
    //for chaining
    public ErrorMessage setMessage(String message){this.message = message;return this;}
    public ErrorMessage setCauses(String causes){this.causes = causes;return this;}
    public ErrorMessage setSource(String source){this.source = source;return this;}
    public ErrorMessageException toException(){
        return  new ErrorMessageException(this);
    }
}
