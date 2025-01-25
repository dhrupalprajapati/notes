package com.gotprint.notes.service;

import com.gotprint.notes.domain.User;
import com.gotprint.notes.dto.ApiResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface UserService {

    Optional<User> findByEmail(String email);

    Mono<ApiResponse> createUser(User user);
}
