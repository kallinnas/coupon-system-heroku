package com.jb.cs.data.db;

import com.jb.cs.data.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByName(String name);

    Company findCompanyByCouponsContains(long id);

}
