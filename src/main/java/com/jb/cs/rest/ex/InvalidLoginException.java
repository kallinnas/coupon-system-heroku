package com.jb.cs.rest.ex;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException(String msg) {
        super(msg);
    }
}
