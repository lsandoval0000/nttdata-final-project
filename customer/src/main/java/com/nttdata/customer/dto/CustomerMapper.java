package com.nttdata.customer.dto;

import com.nttdata.customer.entity.CustomerEntity;
import lombok.Data;

@Data
public class CustomerMapper {
    public Customer toDTO(CustomerEntity customerEntity) {
        Customer dto = new Customer();
        dto.setName(customerEntity.getName());
        dto.setLastName(customerEntity.getLastName());
        dto.setDni(customerEntity.getDni());
        dto.setAddress(customerEntity.getAddress());
        dto.setEmail(customerEntity.getEmail());
        dto.setPhone(customerEntity.getPhone());
        dto.setCustomerType(CustomerType.valueOf(customerEntity.getCustomerTypeentity().toString()));

        return dto;
    }
}
