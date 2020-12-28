package com.jb.cs.service;

import com.jb.cs.data.ex.UnknownRoleForUserException;
import com.jb.cs.data.model.User;
import com.jb.cs.service.ex.UserIsAlreadyExistException;
import com.jb.cs.service.ex.UserIsNotExistException;

public interface UserService {
    User getUserByEmailAndPassword(String email, String password) throws UserIsNotExistException;
    void updateUsersEmail(String email, String password, String newEmail) throws UserIsNotExistException;
    void updateUsersPassword(String email, String password, String newPassword) throws UserIsNotExistException;
    User createUser(String email, String password, int role) throws UserIsAlreadyExistException, UnknownRoleForUserException;
}
