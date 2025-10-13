package com.ecommerce.user.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequest {

    @NotBlank(message = "username is required")
    @Size(max = 40)
    private String userName;

    @NotBlank(message = "message is required")
    @Email
    private String email;

    @NotBlank(message = "mobile no is required")
    @Size(min = 10 , max = 10)
    private String mobileNo;

    @NotBlank(message = "password is required")
    private String password;


}
