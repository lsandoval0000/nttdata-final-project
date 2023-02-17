package com.nttdata.customer.services;
import com.nttdata.customer.dto.Customer;
import com.nttdata.customer.dto.CustomerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomerService {
    private List<Customer> customers = new ArrayList<>();

    /**
     * si existe o no
     * @param dni
     * @return
     */
    public Optional<CustomerType> getCustomerTypeByDni(String dni) {
        return customers.stream()
                .filter(c -> c.getDni().equals(dni))
                .map(Customer::getCustomerType)
                .findFirst();
    }

    /**
     * buscar customerType Personal o Empresarial
     * @param customerType
     * @return
     */

    public List<Customer> getCustomersByType(CustomerType customerType) {
        return customers.stream()
                .filter(c -> c.getCustomerType() == customerType)
                .collect(Collectors.toList());
    }

}












