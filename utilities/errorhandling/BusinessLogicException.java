package com.nnttdata.microserviceactive.utilities.errorhandling;

public class BusinessLogicException extends RuntimeException{
    private static final long serialVersionUID = -5713584292717311040L;

    public BusinessLogicException(String s) {
        super(s);
    }
}
