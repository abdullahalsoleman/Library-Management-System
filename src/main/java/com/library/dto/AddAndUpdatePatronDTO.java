package com.library.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddAndUpdatePatronDTO {
    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name can have a maximum of 50 characters")
    private String name;

    @NotBlank(message = "Contact number is required")
    @Size(min = 10, max = 15, message = "Contact number should be between 10 and 15 digits")
    private String phoneNumber;

    @Email(message = "Invalid email format")
    private String email;

    public AddAndUpdatePatronDTO(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
