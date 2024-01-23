package com.utcn.app.service;

import com.utcn.app.config.JwtService;
import com.utcn.app.dto.CustomerDTO;
import com.utcn.app.mapping.CustomerMapper;
import com.utcn.app.model.Customer;
import com.utcn.app.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        customerDTO.setRole("USER");
        Customer customer = CustomerMapper.mapToCustomer(customerDTO);
        customerRepository.save(customer);
        return CustomerMapper.mapToCustomerDto(customer);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();

        return customers.stream().map(CustomerMapper::mapToCustomerDto).toList();
    }

    @Override
    public CustomerDTO getCustomerById(Integer customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        Customer customer = optionalCustomer.get();
        return CustomerMapper.mapToCustomerDto(customer);
    }

    @Override
    public void deleteCustomer(Integer customerId) {
        boolean exists = customerRepository.existsById(customerId);
        if (!exists) {
            throw new IllegalStateException(
                    "customer with id " + customerId + " does not exist!");
        }
        customerRepository.deleteById(customerId);
    }

    @Override
    public void updateCustomer(CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(customerDTO.getId()).get();
        customer.setEmail(customerDTO.getEmail());
        customer.setUsername(customerDTO.getUsername());
        customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        customerRepository.save(customer);
    }

    @Override
    public CustomerDTO checkForCustomer(CustomerDTO customerDTO) {
        Optional<Customer> optionalCustomer = customerRepository.findByUsername(customerDTO.getUsername());

        if (optionalCustomer.isPresent()) {
            Customer existingCustomer = optionalCustomer.get();

            if (existingCustomer.getPassword().equals(customerDTO.getPassword())) {
                return CustomerMapper.mapToCustomerDto(existingCustomer);
            } else {
                throw new IllegalArgumentException("Invalid credentials");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public String getCustomerUsernameByToken(String token) {
        JwtService jwtService = new JwtService();
        jwtService.extractUsername(token);
        return jwtService.extractUsername(token);
    }
}
