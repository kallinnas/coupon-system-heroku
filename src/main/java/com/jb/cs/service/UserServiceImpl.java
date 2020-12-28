package com.jb.cs.service;

import com.jb.cs.data.db.CompanyRepository;
import com.jb.cs.data.db.CustomerRepository;
import com.jb.cs.data.db.UserRepository;
import com.jb.cs.data.ex.UnknownRoleForUserException;
import com.jb.cs.data.model.Company;
import com.jb.cs.data.model.Customer;
import com.jb.cs.data.model.User;
import com.jb.cs.service.ex.UserIsAlreadyExistException;
import com.jb.cs.service.ex.UserIsNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepo;
    private ApplicationContext context;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ApplicationContext context) {
        this.userRepo = userRepository;
        this.context = context;
    }

    @Override
    public User getUserByEmailAndPassword(String email, String password) throws UserIsNotExistException {
        Optional<User> optUser = userRepo.findByEmailAndPassword(email, password);
        if (!optUser.isPresent()) {
            throw new UserIsNotExistException(String.format("User with such email %s is not exist!", email));
        }
        return optUser.get();
    }

    @Override
    public User createUser(String email, String password, int role) throws UserIsAlreadyExistException, UnknownRoleForUserException {
        Optional<User> optUser = userRepo.findByEmailAndPassword(email, password);
        if (optUser.isPresent()) {
            throw new UserIsAlreadyExistException(String.format("User with such email %s is already exist.", email));
        }
        User user = new User(email, password, role);
        if(user.getClient() instanceof Customer){
            //Call customerRepo to save in it User's client witch is instance of Customer
            CustomerRepository customerRepository = this.context.getBean(CustomerRepository.class);
            customerRepository.save((Customer)user.getClient());
        } else{
            CompanyRepository companyRepository = this.context.getBean(CompanyRepository.class);
            companyRepository.save((Company)user.getClient());
        }
        userRepo.save(user);
        return user;
    }

    @Override
    public void updateUsersEmail(String email, String password, String newEmail) throws UserIsNotExistException {
        User user = getUserByEmailAndPassword(email, password);
        user.setEmail(newEmail);
        userRepo.save(user);
    }

    @Override
    public void updateUsersPassword(String email, String password, String newPassword) throws UserIsNotExistException {
        User user = getUserByEmailAndPassword(email, password);
        user.setPassword(newPassword);
        userRepo.save(user);
    }

}
