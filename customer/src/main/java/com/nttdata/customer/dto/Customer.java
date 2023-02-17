package com.nttdata.customer.dto;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Customer {
    /**
     *
     */
    private String dni;
    private String name;
    private String lastName;
    private String address;
    private String email;
    private LocalDate birthday;
    private String phone;
    private CustomerType customerType;

    /**
     *
     * @param customer
     */
    public Customer(Customer customer) {
        this.name = customer.getName();
        this.lastName = customer.getLastName();
        this.dni = customer.getDni();
        this.address = customer.getAddress();
        this.email = customer.getEmail();
        this.birthday = customer.getBirthday();
        this.phone = customer.getPhone();
        this.customerType = customer.getCustomerType();
    }

    /**
     *
     * @param
     * @return
     */
    public static Customer fromEntity(Customer customer) {
        return new Customer(customer);
    }
}
