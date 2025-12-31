package com.car.management.utils;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;


@MappedSuperclass
public class DefaultDatabaseFields {

    @Column(nullable = false, updatable = false)
    String createdBy;

    @Column(nullable = false, updatable = false)
    String updatedBy;

    @Column(nullable = false, updatable = false)
    LocalDateTime creationDate;

    @Column(nullable = false, updatable = false)
    LocalDateTime updateDate;

    @Column(nullable = false)
    protected Boolean isActive;

    @PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now();
        updateDate = LocalDateTime.now();
        createdBy = CarManagementUtils.getSessionUser();
        updatedBy = CarManagementUtils.getSessionUser();
        isActive = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = LocalDateTime.now();
        updatedBy = CarManagementUtils.getSessionUser();
    }

    // --- DOMAIN METHODS ---

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }
}
