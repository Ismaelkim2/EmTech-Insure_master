package com.jerald.securityapp.service;

import com.jerald.securityapp.dtos.AuthDto;
import com.jerald.securityapp.dtos.UserDto;
import com.jerald.securityapp.entity.Users;
import com.jerald.securityapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTService jwtService;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public AuthDto register(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            return new AuthDto(HttpStatus.CONFLICT.value(), "Email already exists");
        }
        if (userDto.getPassword() == null) {
            return new AuthDto(HttpStatus.BAD_REQUEST.value(), "Password cannot be null");

        }
        Users newUser = new Users();
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(encoder.encode(userDto.getPassword()));
        userRepository.save(newUser);
        return new AuthDto(null, HttpStatus.CREATED.value(), "User Created Successfully");

    }

    public AuthDto verify(UserDto userDto) {
        AuthDto authDto = new AuthDto();
        try {
            Authentication authentication =
                    authenticationManager.
                            authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
            if (authentication.isAuthenticated()) {
                authDto.setJwt(jwtService.generateToken(userDto.getEmail()));
                authDto.setStatusCode(HttpStatus.OK.value());
                authDto.setMessage("Verified");
                return authDto;
            }

        } catch (BadCredentialsException ex) {
            authDto.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            authDto.setMessage("Check credentials");
        }
        return authDto;

    }
}

