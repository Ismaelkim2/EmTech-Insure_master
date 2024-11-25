package com.jerald.securityapp.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthDto {
    private String jwt;
    private int statusCode;
    private String message;

    public AuthDto(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
