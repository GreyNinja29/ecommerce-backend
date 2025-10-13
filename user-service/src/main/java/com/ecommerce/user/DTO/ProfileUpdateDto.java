package com.ecommerce.user.DTO;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileUpdateDto {

    @Nullable
    @Size(max = 40)
    private String userName;

    @Nullable
    @Email
    private String email;

    @Nullable
    @Size(min = 10 , max = 10)
    private String mobileNo;
}
