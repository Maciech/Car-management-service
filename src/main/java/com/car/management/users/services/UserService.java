package com.car.management.users.services;


import com.car.management.configuration.JWT.JwtService;
import com.car.management.users.models.UserEntity;
import com.car.management.users.models.UserModel;
import com.car.management.users.repository.UserRepository;
import com.car.management.utils.default_exceptions.EmailOrPasswordIncorrectException;
import com.car.management.utils.default_exceptions.EntityAlreadyExistsException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    AuthenticationManager authenticationManager;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    UserRepository userRepository;
    JwtService jwtService;

    public String verifyUser(UserModel loginRequest) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            if (authenticate.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authenticate.getPrincipal();

                return jwtService.generateToken(Objects.requireNonNull(userDetails));
            }
        } catch (AuthenticationException e) {
            throw new EmailOrPasswordIncorrectException();
        }
        return "";
    }

    @Transactional
    public void registerUser(UserModel registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EntityAlreadyExistsException("Użytkownik z mailem " + registerRequest.getEmail() + " już istnieje");
        }
        UserEntity newUser = new UserEntity();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(encoder.encode(registerRequest.getPassword()));
        newUser.setRole(registerRequest.getRole());

        userRepository.saveAndFlush(newUser);
    }
}
