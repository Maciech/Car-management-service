package com.car.management.user;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    Argon2 argon2 = Argon2Factory.create();

    public void register(UserDto request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        char[] password = request.getPassword().toCharArray();
        UserEntity entity = UserEntity.builder()
                .username(request.getUsername())
                .password(argon2.hash(10, 65536, 1, password))
                .enabled(true)
                .build();

        userRepository.save(entity);
        argon2.wipeArray(password);
    }

    public void login(UserDto request) {
        UserEntity entity = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        char[] requestPassword = request.getPassword().toCharArray();
        argon2.verify(entity.getPassword(), requestPassword);
    }

    public void passwordChange(UserPassChangeDto request) {
        UserEntity entity = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        char[] requestOldPass = request.getOldPassword().toCharArray();
        verifyPasswords(entity.getPassword(), requestOldPass, "Current password is incorrect");

        char[] newPasswordChar = request.getNewPassword().toCharArray();
        verifyPasswords(entity.getPassword(), newPasswordChar, "New password must be different from current password");

        try {
            String newPassword = argon2.hash(10, 65536, 1, newPasswordChar);
            entity.setPassword(newPassword);
            userRepository.save(entity);
        } finally {
            argon2.wipeArray(newPasswordChar);
        }
    }

    private void verifyPasswords(String password, char[] passwordToCompare, String message) {
        try {
            if (argon2.verify(password, passwordToCompare)) {
                throw new SecurityException(message);
            }
        } finally {
            argon2.wipeArray(passwordToCompare);
        }
    }


}
