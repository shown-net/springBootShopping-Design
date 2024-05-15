package com.example.pojo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordDto {
    Integer accountID;
    @Size(min = 3,max = 20)
    private String accountName;
    @NotEmpty
    @Size(min = 8,max = 20)
    String oldPassword;
    @NotEmpty
    @Size(min = 8,max = 20)
    String newPassword;
}
