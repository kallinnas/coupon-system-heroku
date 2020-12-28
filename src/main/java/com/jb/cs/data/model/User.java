package com.jb.cs.data.model;

import com.jb.cs.data.ex.UnknownRoleForUserException;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    @Column(name = "password")
    private String password;

    @Any(metaColumn = @Column(name = "role"))
    @AnyMetaDef(idType = "long", metaType = "int",
            metaValues = {
            @MetaValue(value = "1", targetEntity = Customer.class),
            @MetaValue(value = "2", targetEntity = Company.class),
    })
    @JoinColumn(name = "client_id")
    private Client client;

    public User() {
        /*Empty*/
    }

    public User(String email, String password, int role) throws UnknownRoleForUserException {
        this.email = email;
        this.password = password;
        this.id = 0;
        if (role == 1) {
            this.client = new Customer();
            this.client.setId(0);
        } else if (role == 2) {
            this.client = new Company();
            this.client.setId(0);
        } else
            throw new UnknownRoleForUserException(String
                    .format("To create user-customer set a role as - 1, for user-company -2. Unknown role %d.", role));
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}
