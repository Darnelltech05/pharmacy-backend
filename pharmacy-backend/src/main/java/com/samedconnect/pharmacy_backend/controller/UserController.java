package com.samedconnect.pharmacy_backend.controller;

import com.samedconnect.pharmacy_backend.dto.request.UpdateProfileRequest;
import com.samedconnect.pharmacy_backend.dto.response.UserResponse;
import com.samedconnect.pharmacy_backend.entity.User;
import com.samedconnect.pharmacy_backend.service.UserService;
import com.samedconnect.pharmacy_backend.utils.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<Response<UserResponse>> getProfile(@AuthenticationPrincipal User user) {
        UserResponse profile = userService.getUserProfile(user.getId());
        return ResponseEntity.ok(Response.success("Profile retrieved successfully", profile));
    }

    @PutMapping("/profile")
    public ResponseEntity<Response<UserResponse>> updateProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateProfileRequest request) {
        UserResponse updatedProfile = userService.updateProfile(user.getId(), request);
        return ResponseEntity.ok(Response.success("Profile updated successfully", updatedProfile));
    }
}