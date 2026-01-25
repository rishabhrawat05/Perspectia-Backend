package com.perspectia.perspectiabackend.services;

import com.perspectia.perspectiabackend.exceptions.UserNotFoundException;
import com.perspectia.perspectiabackend.models.User;
import com.perspectia.perspectiabackend.repositories.UserRepository;
import com.perspectia.perspectiabackend.responses.UserResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getUser(UUID userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        userResponse.setEmail(user.getEmail());
        userResponse.setName(user.getName());
        userResponse.setEmailVerified(user.isVerified());
        userResponse.setProvider(user.getAuthProviders().stream().findFirst().orElse(null));
        return userResponse;
    }

    public UserResponse findByEmail(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setName(user.getName());
        userResponse.setEmailVerified(user.isVerified());
        userResponse.setProvider(user.getAuthProviders().stream().findFirst().orElse(null));
        return userResponse;
    }




}
