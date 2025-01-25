package com.gotprint.notes.controller;

import com.gotprint.notes.domain.User;
import com.gotprint.notes.dto.ApiResponse;
import com.gotprint.notes.service.UserService;
import com.gotprint.notes.util.ApiResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Mono<ApiResponse>> createUser(@Valid @RequestBody User user, Errors errors) {
        if (errors.hasErrors()) {
           return ResponseEntity.badRequest().body(ApiResponseUtil.errorResponse(errors));
        }
        return ResponseEntity.ok(userService.createUser(user));
    }

}
