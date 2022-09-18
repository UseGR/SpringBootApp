package ru.galeev.springcourse.dto;

import lombok.Data;

@Data
public class RegisterResponseDTO {
    private String username;
    private String token;
}
