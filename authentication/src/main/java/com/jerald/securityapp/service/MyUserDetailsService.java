package com.jerald.securityapp.service;

import com.jerald.securityapp.entity.UserPrincipal;
import com.jerald.securityapp.entity.Users;
import com.jerald.securityapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(username);
        if (user== null){
            System.out.println("User not found");
            throw  new UsernameNotFoundException("User not found");
        }

        return new UserPrincipal(user);
    }
}
