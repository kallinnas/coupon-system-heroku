package com.jb.cs.rest.ex;

public class UnknownEmailOrPasswordException extends Exception {
    public UnknownEmailOrPasswordException(String msg) {
        super(msg);
    }
}
