package com.perspectia.perspectiabackend.controllers;

import com.perspectia.perspectiabackend.responses.UserResponse;
import com.perspectia.perspectiabackend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/perspectica/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get")
    public ResponseEntity<UserResponse> getUser(@RequestParam UUID userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }


}
