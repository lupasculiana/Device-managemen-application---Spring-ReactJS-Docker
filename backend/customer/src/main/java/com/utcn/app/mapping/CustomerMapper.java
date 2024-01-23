package com.utcn.app.mapping;


import com.utcn.app.dto.CustomerDTO;
import com.utcn.app.model.Customer;
import com.utcn.app.model.RoleEnum;

public class CustomerMapper {
    public static CustomerDTO mapToCustomerDto(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getEmail(),
                customer.getUsername(),
                customer.getPassword(),
                fromEnumToString(customer.getRole())
        );
    }

    public static Customer mapToCustomer(CustomerDTO customerDTO) {
        return new Customer(
                customerDTO.getId(),
                customerDTO.getEmail(),
                customerDTO.getUsername(),
                customerDTO.getPassword(),
                fromStringToEnum(customerDTO.getRole())
        );
    }

    public static String fromEnumToString(RoleEnum roleEnum){
        if(roleEnum.equals(RoleEnum.ADMIN)){
            return "ADMIN";
        }else{
            return "USER";
        }
    }

    public static RoleEnum fromStringToEnum(String role){
        if(role.equals("ADMIN")){
            return RoleEnum.ADMIN;
        }else{
            return RoleEnum.USER;
        }
    }
}