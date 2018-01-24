package com.cundong.recyclerview;

/**
 * Created by teknopc on 18-May-16.
 */
public class CustomException extends RuntimeException {
    public CustomException (String message){

        super(message);

    }

   //Constructor that accepts an error message and a Throwable
    public CustomException(String message, Throwable cause){

        super (message, cause);

    }
}
