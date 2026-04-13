package com.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    private String name;
    private String email;

    // ✅ STORE ONLY URL (NOT FILE)
    private String profilePhotoUrl;

    private boolean isVerified;

    private String otp;
    private Long otpExpiry;
}