package com.perspectia.perspectiabackend.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String email;
    private UUID id;
    private String name;
    private boolean emailVerified;
    private String provider;
}
