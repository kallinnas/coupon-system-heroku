package com.jb.cs.data.db;

import com.jb.cs.data.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByFirstName(String firstName);
    Optional<Customer> findByLastName(String lastName);
    List<Customer> findAllCustomersByCoupons(long coupon_id);

}
