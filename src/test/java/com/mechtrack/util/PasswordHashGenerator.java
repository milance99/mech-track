package com.mechtrack.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "password";
        String hash = encoder.encode(password);
        System.out.println("Password: " + password);
        System.out.println("BCrypt hash: " + hash);
        System.out.println("Verification: " + encoder.matches(password, hash));

        // Test the current hash
        String currentHash = "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.";
        System.out.println("Current hash matches 'password': " + encoder.matches(password, currentHash));
    }
}
