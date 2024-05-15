package com.example.pojo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {
    @NotEmpty
    @Size(min = 3,max = 20)
    private String accountName;
    @NotEmpty
    @Size(min = 8,max = 20)
    private String password;
    @Email
    private String email;
}
