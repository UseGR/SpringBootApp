package ru.galeev.springcourse.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String username;
    private String token;
}
