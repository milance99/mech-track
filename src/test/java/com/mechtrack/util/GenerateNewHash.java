package com.mechtrack.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateNewHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "password123";
        String hash = encoder.encode(password);
        System.out.println("Generated BCrypt hash for 'password123': " + hash);
        System.out.println("Verification test: " + encoder.matches(password, hash));
    }
}
