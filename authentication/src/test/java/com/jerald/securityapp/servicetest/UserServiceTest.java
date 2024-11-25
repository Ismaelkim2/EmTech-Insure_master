package com.jerald.securityapp.servicetest;

import com.jerald.securityapp.dtos.AuthDto;
import com.jerald.securityapp.dtos.UserDto;
import com.jerald.securityapp.entity.Users;
import com.jerald.securityapp.repository.UserRepository;
import com.jerald.securityapp.service.JWTService;
import com.jerald.securityapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JWTService jwtService;
    @InjectMocks
     private UserService userService;

    private final BCryptPasswordEncoder encoder =new BCryptPasswordEncoder(12);

@Test
    void testRegister_EmailAlreadyExists (){
        UserDto userDto = new UserDto();
        userDto.setEmail("existing@example.com");
        userDto.setPassword("password123");

        // mock behavior
        Mockito.when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

        AuthDto response = userService.register(userDto);

        assertEquals(HttpStatus.CONFLICT.value(), response.getStatusCode());
        assertEquals("Email already exists", response.getMessage());
    }
    @Test
    void testRegister_PasswordIsNull(){
        UserDto userDto = new UserDto();

        userDto.setEmail("new@example.com");
        AuthDto response = userService.register(userDto);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        assertEquals("Password cannot be null", response.getMessage());

    }
    @Test
    void testRegister_SuccessfulRegistration(){

    UserDto userDto = new UserDto();
    userDto.setEmail("new@example.com");
    userDto.setPassword("password123");

    // mock behavior

        Mockito.when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);

        Users savedUser = new Users();
        savedUser.setEmail(userDto.getEmail());
        savedUser.setPassword(encoder.encode(userDto.getPassword()));
        Mockito.when(userRepository.save(Mockito.any(Users.class))).thenReturn(savedUser);


        AuthDto response = userService.register(userDto);
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertEquals("User Created Successfully", response.getMessage());

        // verify user is saved

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(Users.class));

    }
    // test verification of user
    @Test
     void testVerify_InvalidCredentials(){
    UserDto userDto = new UserDto();
    userDto.setEmail("user@example.com");
    userDto.setPassword("wrongPassword");

    //mock behavior

         Mockito.when(authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(userDto.getEmail(),userDto.getPassword())
         )).thenThrow(new BadCredentialsException("Bad credentials"));

         AuthDto response = userService.verify(userDto);
         assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
         assertEquals("Check credentials", response.getMessage());
     }
@Test
     void testVerify_ValidCredentials(){
    UserDto userDto = new UserDto() ;
    userDto.setEmail("usere@example.com");
    userDto.setPassword("correctPassword");

    //mock behavior

         Authentication mockAuthentication = Mockito.mock(Authentication.class);
         Mockito.when(mockAuthentication.isAuthenticated()).thenReturn(true);
         Mockito.when(authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(userDto.getEmail(),userDto.getPassword())
         )).thenReturn(mockAuthentication);

         Mockito.when(jwtService.generateToken(userDto.getEmail())).thenReturn("mockJwtToken");

         AuthDto response = userService.verify(userDto);


         assertEquals(HttpStatus.OK.value(), response.getStatusCode());
         assertEquals("Verified", response.getMessage());
         assertEquals("mockJwtToken", response.getJwt());

     }

}
