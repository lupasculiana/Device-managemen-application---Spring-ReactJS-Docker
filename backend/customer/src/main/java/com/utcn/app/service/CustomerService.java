package com.utcn.app.service;


import com.utcn.app.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {
    CustomerDTO createCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> getAllCustomers();

    CustomerDTO getCustomerById(Integer customerId);

    void deleteCustomer(Integer customerId);

    void updateCustomer(CustomerDTO customerDTO);

    CustomerDTO checkForCustomer(CustomerDTO customerDTO);

}
