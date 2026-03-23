package com.car.management.users.controller;

import com.car.management.users.models.UserModel;
import com.car.management.users.services.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authorizeUser(@RequestBody UserModel loginRequest) {
        return ResponseEntity.ok().body(userService.verifyUser(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserModel registerRequest) {
        userService.registerUser(registerRequest);
        return ResponseEntity.accepted().build();
    }
}
