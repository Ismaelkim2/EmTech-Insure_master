package com.jerald.securityapp.controller;

import com.jerald.securityapp.dtos.AuthDto;
import com.jerald.securityapp.dtos.UserDto;
import com.jerald.securityapp.entity.Users;
import com.jerald.securityapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody UserDto userDto){
        AuthDto authDto = userService.register(userDto);
        return ResponseEntity.status(authDto.getStatusCode()).body(authDto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDto> login(@RequestBody UserDto userDto){
        AuthDto authDto = userService.verify(userDto);

        return ResponseEntity.status(authDto.getStatusCode()).body(authDto);
    }
}
