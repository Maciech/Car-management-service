package com.car.management.user;

import lombok.Data;

@Data
public class UserPassChangeDto {

    private String username;
    private String oldPassword;
    private String newPassword;
}
