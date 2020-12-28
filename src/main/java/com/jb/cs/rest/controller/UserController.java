package com.jb.cs.rest.controller;

import com.jb.cs.data.ex.UnknownRoleForUserException;
import com.jb.cs.data.model.Client;

import com.jb.cs.data.model.Company;
import com.jb.cs.data.model.Token;
import com.jb.cs.data.model.User;
import com.jb.cs.service.CompanyService;
import com.jb.cs.service.UserService;
import com.jb.cs.service.ex.UserIsAlreadyExistException;
import com.jb.cs.service.ex.UserIsNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class UserController {
    private UserService service;
    private ApplicationContext context;

    @Autowired
    public UserController(UserService service, ApplicationContext context) {
        this.service = service;
        this.context = context;
    }



    @GetMapping("/user/getAllCompanies")
    public ResponseEntity<List<Company>> getAllCompanies() {
        CompanyService service = context.getBean(CompanyService.class);
        List<Company> companies = service.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    // REGISTRATION AND LOGIN
    @GetMapping("/user/{email}/{password}")
    public ResponseEntity<User> login(@PathVariable String email,
                                      @PathVariable String password) throws UserIsNotExistException {
        User user = service.getUserByEmailAndPassword(email, password);
        if (user == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping("reg/{email}/{password}/{role}")
    public ResponseEntity<Token> registration(@PathVariable String email,
                                              @PathVariable String password,
                                              @PathVariable int role) throws UserIsAlreadyExistException, UnknownRoleForUserException {
        service.createUser(email, password, role);
        LoginController loginController = context.getBean(LoginController.class);
        return loginController.login(email, password);
    }


    // UPDATE ACCOUNT
    @PostMapping("changeEmail/{email}/{password}/{newEmail}")
    public ResponseEntity<String> updateEmail(
            @PathVariable String email,
            @PathVariable String password,
            @PathVariable String newEmail) throws UserIsNotExistException {
        service.updateUsersEmail(email, password, newEmail);
        String ok = "The email has been changed successfully";
        return ResponseEntity.ok(ok);
    }

    @PostMapping("changePassword/{email}/{password}/{newPassword}")
    public ResponseEntity<String> changePassword(@PathVariable String email,
                                                 @PathVariable String password,
                                                 @PathVariable String newPassword) throws UserIsNotExistException {
        service.updateUsersPassword(email, password, newPassword);
        String ok = "The password has been changed successfully";
        return ResponseEntity.ok(ok);
    }


}

