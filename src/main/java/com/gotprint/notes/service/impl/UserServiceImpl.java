package com.gotprint.notes.service.impl;

import com.gotprint.notes.domain.User;
import com.gotprint.notes.dto.ApiResponse;
import com.gotprint.notes.repository.UserRepository;
import com.gotprint.notes.service.UserService;
import com.gotprint.notes.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Mono<ApiResponse> createUser(User user) {
        logger.info("createUser :: create user with user email: {}", user.getEmail());
        return Mono.fromCallable(() -> findByEmail(user.getEmail())
                .orElse(null))
                .flatMap(existingUser -> ApiResponseUtil.errorResponse("User already present with given email id."))
                .switchIfEmpty(Mono.defer(() -> {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    save(user);
                    return ApiResponseUtil.successResponse("User created successfully!");
                }));
    }
}
