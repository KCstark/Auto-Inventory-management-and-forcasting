package com.inventory_forcasting.models;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsersDTO {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Roles role;
    private String token;
}
