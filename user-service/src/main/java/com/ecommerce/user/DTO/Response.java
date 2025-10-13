package com.ecommerce.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response {

    private Long id;
    private String email;
    private String username;
    private String jwt;
    private String role;



}
