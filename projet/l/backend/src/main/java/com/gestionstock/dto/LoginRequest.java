package com.gestionstock.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String motPasse;
}
