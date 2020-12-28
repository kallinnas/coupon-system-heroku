package com.jb.cs.service.ex;

public class UserIsNotExistException extends Exception {
    public UserIsNotExistException(String msg) {
        super(msg);
    }
}
