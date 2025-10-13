package com.ecommerce.user.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @Email(message = "Enter valid email!!")
    @NotBlank(message = "Email is required!!")
    private String email;

    @NotBlank(message = "password can not be empty!!")
    private String password;

}
