package com.jb.cs.rest;

import com.jb.cs.data.db.UserRepository;
import com.jb.cs.data.model.*;
import com.jb.cs.rest.ex.InvalidLoginException;
import com.jb.cs.rest.ex.InvalidSessionTypeException;
import com.jb.cs.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Optional;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserSystem {
    private ApplicationContext context;
    private UserRepository userRepository;
    private Thread dailyTask;
    private Thread tokenTask;

    @Autowired
    public UserSystem(ApplicationContext context, UserRepository userRepository, DailyTask dailyTask,TokenTask tokenTask) {
        this.context = context;
        this.userRepository = userRepository;
        this.dailyTask = new Thread(dailyTask);
        this.tokenTask = new Thread(tokenTask);
    }

    @PostConstruct
    private void startTask() {
        dailyTask.start();
    }

    @PreDestroy
    private void stopTasks() {
        DailyTask.stopDailyTask();
        TokenTask.stopTokenTask();
        System.out.println("@PreDestroy method");
    }

    public ClientSession createClientSession(String email, String password) {

        if (!tokenTask.isAlive()) {
            tokenTask.start();
        }
        // ADMIN DATA
        String adminEmail = "admin";
        String adminPass = "777";

        if (email.equals(adminEmail) && password.equals(adminPass)) {
            AdminService service = context.getBean(AdminService.class);
            ClientSession session = context.getBean(ClientSession.class);
            session.setRole(3);
            session.setAdminService(service);
            session.accessed();
            return session;
        }

        Optional<User> optUser = userRepository.findByEmailAndPassword(email, password);
        if (!optUser.isPresent()) {
            throw new InvalidLoginException(String.format(
                    "Invalid login with email: %s and password: %s", email, password));
        }

        Client client = optUser.get().getClient();

        if (client instanceof Customer) {
            return getCustomerSession(client);
        } else if (client instanceof Company) {
            return getCompanySession(client);
        }
        throw new InvalidSessionTypeException("\nUnable to identify type like: %s for client session.\n" +
                        "Stay on your place! The special forces were been sent to you.");
    }

    // METHODS TO DEFINE USER FOR THE SESSION
    private ClientSession getCustomerSession(Client client) {
        CustomerService service = context.getBean(CustomerService.class);
        service.setCustomer_id(client.getId());
        ClientSession session = context.getBean(ClientSession.class);
        session.setRole(1);
        session.setCustomerService(service);
        session.accessed();
        return session;
    }

    private ClientSession getCompanySession(Client client) {
        CompanyService service = context.getBean(CompanyService.class);
        service.setCompany_id(client.getId());
        ClientSession session = context.getBean(ClientSession.class);
        session.setRole(2);
        session.setCompanyService(service);
        session.accessed();
        return session;
    }
}
