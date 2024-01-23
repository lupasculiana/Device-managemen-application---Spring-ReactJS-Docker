package com.utcn.app.dto;

import com.utcn.app.model.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private Integer id;
    private String email;
    private String username;
    private String password;
    private RoleEnum role;
}

