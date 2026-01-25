package com.perspectia.perspectiabackend.services;

import com.perspectia.perspectiabackend.exceptions.UserNotFoundException;
import com.perspectia.perspectiabackend.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
        // TODO Auto-generated method stub
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User Not Found Exception"));
    }

}