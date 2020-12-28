package com.jb.cs.rest.controller;

import com.jb.cs.data.db.UserRepository;
import com.jb.cs.data.model.Token;
import com.jb.cs.rest.ClientSession;
import com.jb.cs.rest.ex.InvalidLoginException;
import com.jb.cs.rest.UserSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class LoginController {

    private static final int LENGTH_TOKEN = 15;
    private Map<String, ClientSession> tokensMap;
    private UserSystem system;
    private UserRepository userRepo;
    private ApplicationContext context;

    @Autowired
    public LoginController(@Qualifier("tokens") Map<String, ClientSession> tokensMap, UserSystem system,
                           UserRepository repository, ApplicationContext context) {
        this.tokensMap = tokensMap;
        this.system = system;
        this.userRepo = repository;
        this.context = context;
    }


    @PostMapping("/login/{login}/{password}")
    public ResponseEntity<Token> login(@PathVariable String login, @PathVariable String password) throws InvalidLoginException {
            ClientSession session = system.createClientSession(login, password);
            String token = generateToken();
            tokensMap.put(token, session);
            Token myToken = new Token();
            myToken.setToken(token);
            return ResponseEntity.ok(myToken);
    }

    @GetMapping("/{token}/getAccount")
    public ResponseEntity<Integer> customersAccount(@PathVariable String token){
        ClientSession session = tokensMap.get(token);
        return ResponseEntity.ok(session.getRole());
    }

    private String generateToken() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, LENGTH_TOKEN);
    }
}













/*  @PostMapping("/reg")
    public ResponseEntity<User> registration(@RequestParam String email,
                                             @RequestParam String password,
                                             @RequestParam int role) throws UserIsAlreadyExistException, UnknownRoleForUserException {
        //Check if the User already exist
        Optional<User> optUser = userRepo.findByEmailAndPassword(email, password);
        if (optUser.isPresent()) {
            throw new UserIsAlreadyExistException(String.format("User with such email %s is already exist", email));
        }
        //Using constructor to define the role 1 - Customer/ "2" - Company
        User user = new User(email, password, role);
        //If User's client is ...
        if(user.getClient() instanceof Customer){
            //Call customerRepo to save in it User's client witch is instance of Customer
            CustomerRepository customerRepository = this.context.getBean(CustomerRepository.class);
            customerRepository.save((Customer)user.getClient());
        } else{
            CompanyRepository companyRepository = this.context.getBean(CompanyRepository.class);
            companyRepository.save((Company)user.getClient());
        }
        //After saving User's client to the repository of one of the target entities(Customer/Company)
        //Save User to its repository
        userRepo.save(user);
        return ResponseEntity.ok(user);
    }*/
