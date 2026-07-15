package com.samedconnect.pharmacy_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String idNumber;
    private String medicalAidNumber;
    private String medicalAidName;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String clinicAffiliation;
}