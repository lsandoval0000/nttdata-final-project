package com.nttdata.customer.repository;

import com.nttdata.customer.dto.Customer;
import com.nttdata.customer.dto.CustomerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     *
     * @param lastName
     * @return
     */


    List<Customer> findByLastName(String lastName);

    List<Customer> findByCustomerType(CustomerType customerType);

    Optional<Customer> findByDni(String dni);
    List<Customer> findAllByCustomerType(CustomerType customerType);




}

