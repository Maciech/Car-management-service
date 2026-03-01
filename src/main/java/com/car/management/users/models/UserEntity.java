package com.car.management.users.models;

import com.car.management.utils.DefaultDatabaseFields;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity extends DefaultDatabaseFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;

    @Column(columnDefinition = "varchar(100)")
    String email;

    @JsonIgnore
    String password;

    @EnumeratedValue
    UserRole role;

    @Column(columnDefinition = "varchar(15)")
    String phone;

    @Column(columnDefinition = "varchar(50)")
    String address;
}