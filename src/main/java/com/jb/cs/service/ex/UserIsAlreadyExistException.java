package com.jb.cs.service.ex;

public class UserIsAlreadyExistException extends Exception {
    public UserIsAlreadyExistException(String msg) {
        super(msg);
    }
}
