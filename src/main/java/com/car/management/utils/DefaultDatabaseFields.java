package com.car.management.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class DefaultDatabaseFields {
    String createdBy;
    String updatedBy;
    LocalDateTime creationDate;
    LocalDateTime updateDate;
    boolean isActive;
}
