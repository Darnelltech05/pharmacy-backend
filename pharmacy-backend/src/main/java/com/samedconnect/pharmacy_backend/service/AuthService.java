package com.samedconnect.pharmacy_backend.service;

import com.samedconnect.pharmacy_backend.dto.request.LoginRequest;
import com.samedconnect.pharmacy_backend.dto.request.RegisterRequest;
import com.samedconnect.pharmacy_backend.dto.response.AuthResponse;
import com.samedconnect.pharmacy_backend.entity.User;
import com.samedconnect.pharmacy_backend.entity.UserProfile;
import com.samedconnect.pharmacy_backend.exception.BadRequestException;
import com.samedconnect.pharmacy_backend.repository.UserProfileRepository;
import com.samedconnect.pharmacy_backend.repository.UserRepository;
import com.samedconnect.pharmacy_backend.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Check if username exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        // Check if email exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        // Create user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));  // BCrypt encrypt
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(User.Role.CUSTOMER);  // Default role
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        // Create user profile
        UserProfile profile = new UserProfile();
        profile.setUser(savedUser);
        profile.setIdNumber(request.getIdNumber());
        profile.setMedicalAidNumber(request.getMedicalAidNumber());
        profile.setMedicalAidName(request.getMedicalAidName());
        profile.setEmergencyContactName(request.getEmergencyContactName());
        profile.setEmergencyContactPhone(request.getEmergencyContactPhone());
        profile.setClinicAffiliation(request.getClinicAffiliation());

        userProfileRepository.save(profile);

        // Generate JWT token
        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Get user
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("User not found"));

        // Generate JWT token
        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}