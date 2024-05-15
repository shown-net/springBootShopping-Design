package com.example.pojo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrowserLog {
    private Integer id;
    private Integer userID;
    @NotNull
    private String kind;
    @NotNull
    @Min(value = 5)
    private Integer browseTime;
}
