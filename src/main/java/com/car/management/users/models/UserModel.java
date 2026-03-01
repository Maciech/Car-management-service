package com.car.management.users.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserModel {

    Long userId;
    String email;
    String password;
    UserRole role;
    String phone;
    String address;
}