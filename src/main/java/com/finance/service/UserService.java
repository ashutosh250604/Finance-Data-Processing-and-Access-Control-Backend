package com.finance.service;

import com.finance.dto.request.UserUpdateRequest;
import com.finance.dto.response.UserResponse;
import com.finance.entity.User;
import com.finance.enums.Role;
import com.finance.enums.UserStatus;
import com.finance.exception.BadRequestException;
import com.finance.exception.ResourceNotFoundException;
import com.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User Service - Handles user management operations
 * 
 * All user management operations are restricted to ADMIN role
 * (enforced in controller with @PreAuthorize)
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all users
     */
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get user by ID
     */
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return UserResponse.fromEntity(user);
    }

    /**
     * Get user by username
     */
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return UserResponse.fromEntity(user);
    }

    /**
     * Update user details
     */
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // Update only non-null fields
        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            // Check if new username is already taken
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new BadRequestException("Username is already taken");
            }
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            // Check if new email is already taken
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new BadRequestException("Email is already registered");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }

        user = userRepository.save(user);
        return UserResponse.fromEntity(user);
    }

    /**
     * Change user status (activate/deactivate)
     */
    public UserResponse changeUserStatus(Long id, UserStatus status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        user.setStatus(status);
        user = userRepository.save(user);
        return UserResponse.fromEntity(user);
    }

    /**
     * Change user role
     */
    public UserResponse changeUserRole(Long id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        user.setRole(role);
        user = userRepository.save(user);
        return UserResponse.fromEntity(user);
    }

    /**
     * Delete user
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Get all active users
     */
    public List<UserResponse> getActiveUsers() {
        return userRepository.findByStatus(UserStatus.ACTIVE)
                .stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
