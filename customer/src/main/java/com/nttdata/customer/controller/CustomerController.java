package com.nttdata.customer.controller;
import com.nttdata.customer.services.CustomerService;
import com.nttdata.customer.dto.Customer;
import com.nttdata.customer.dto.CustomerType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class CustomerController {
    private final CustomerService customerService;


    /**
     *
     * @param dni
     * @return
     */
    @GetMapping("/search/{dni}")
    public String getCustomerTypeByDni(@PathVariable String dni) {
        Optional<CustomerType> customerType = customerService.getCustomerTypeByDni(dni);
        return customerType.isPresent() ? "El tipo de cliente es " + customerType.get() : "Cliente no encontrado";
    }

    /**
     * buscar customerType Personal o Empresarial
     * @param customerType
     * @return
     */
    @GetMapping("/search_type/{customerType}")
    public ResponseEntity<List<Customer>> getCustomersByType(@RequestParam CustomerType customerType) {
        List<Customer> customers = customerService.getCustomersByType(customerType);
        if (customers.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(customers);
        }
    }

}








