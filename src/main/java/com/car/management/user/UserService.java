package com.car.management.user;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
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
                .orElseThrow(() -> new RuntimeException("User not found"));

        char[] requestPassword = request.getPassword().toCharArray();
        argon2.verify(entity.getPassword(), requestPassword);
    }

}
