package com.samedconnect.pharmacy_backend.service;

import com.samedconnect.pharmacy_backend.dto.request.UpdateProfileRequest;
import com.samedconnect.pharmacy_backend.dto.response.UserResponse;
import com.samedconnect.pharmacy_backend.entity.User;
import com.samedconnect.pharmacy_backend.entity.UserProfile;
import com.samedconnect.pharmacy_backend.exception.ResourceNotFoundException;
import com.samedconnect.pharmacy_backend.repository.UserProfileRepository;
import com.samedconnect.pharmacy_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public UserResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElse(new UserProfile());

        return mapToUserResponse(user, profile);
    }

    @Transactional
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Update basic info
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());

        // Get or create profile
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElse(new UserProfile());
        profile.setUser(user);
        profile.setIdNumber(request.getIdNumber());
        profile.setMedicalAidNumber(request.getMedicalAidNumber());
        profile.setMedicalAidName(request.getMedicalAidName());
        profile.setEmergencyContactName(request.getEmergencyContactName());
        profile.setEmergencyContactPhone(request.getEmergencyContactPhone());
        profile.setClinicAffiliation(request.getClinicAffiliation());

        userRepository.save(user);
        userProfileRepository.save(profile);

        return mapToUserResponse(user, profile);
    }

    private UserResponse mapToUserResponse(User user, UserProfile profile) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().name())
                .isActive(user.getIsActive())
                .idNumber(profile.getIdNumber())
                .medicalAidNumber(profile.getMedicalAidNumber())
                .medicalAidName(profile.getMedicalAidName())
                .emergencyContactName(profile.getEmergencyContactName())
                .emergencyContactPhone(profile.getEmergencyContactPhone())
                .clinicAffiliation(profile.getClinicAffiliation())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}