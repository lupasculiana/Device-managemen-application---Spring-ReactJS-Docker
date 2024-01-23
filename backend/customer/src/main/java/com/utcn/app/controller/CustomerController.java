package com.utcn.app.controller;

import com.utcn.app.dto.CustomerDTO;
import com.utcn.app.service.CustomerServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/customers")
@RequiredArgsConstructor
@CrossOrigin
public class CustomerController {
    private final CustomerServiceImpl customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin("http://localhost:3000/")
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        customerDTO.setRole("USER");
        log.info("new customer registration {}", customerDTO);
        CustomerDTO loggedInCustomer = customerService.createCustomer(customerDTO);
        return ResponseEntity.ok(loggedInCustomer);
    }

    @PostMapping("/login")
    @CrossOrigin("http://localhost:3000/")
    public ResponseEntity<CustomerDTO> loginCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            CustomerDTO loggedInCustomer = customerService.checkForCustomer(customerDTO);
            return ResponseEntity.ok(loggedInCustomer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin("http://localhost:3000/")
    @PreAuthorize("hasRole('ADMIN')")
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin("http://localhost:3000/")
    public CustomerDTO getCustomerById(@PathVariable("id") Integer customerId) {
        return customerService.getCustomerById(customerId);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin("http://localhost:3000/")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateCustomer(@PathVariable("id") Integer customerId,
                               @RequestBody CustomerDTO customerDTO) {
        customerDTO.setId(customerId);
        customerService.updateCustomer(customerDTO);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin("http://localhost:3000/")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCustomer(@PathVariable("id") Integer customerId) {
        customerService.deleteCustomer(customerId);
    }

    @GetMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin("http://localhost:3000/")
    public String getCustomerIdByToken(@RequestHeader("Authorization") String token) {
        System.out.printf("token: %s\n", token);
        String username = customerService.getCustomerUsernameByToken(token);
        System.out.printf(username + "\n" );
        return username;
    }

}

