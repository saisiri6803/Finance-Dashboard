package com.finance.service;

import com.finance.dto.request.UpdateUserRequest;
import com.finance.dto.response.UserResponse;
import com.finance.exception.ResourceNotFoundException;
import com.finance.model.User;
import com.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::from).toList();
    }

    public UserResponse getUserById(Long id) {
        return UserResponse.from(findUser(id));
    }

    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest req) {
        User user = findUser(id);
        if (req.getName() != null)   user.setName(req.getName());
        if (req.getRole() != null)   user.setRole(req.getRole());
        if (req.getActive() != null) user.setActive(req.getActive());
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }
}