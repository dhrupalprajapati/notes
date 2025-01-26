package com.gotprint.notes.controller;

import com.gotprint.notes.config.SecurityConfig;
import com.gotprint.notes.domain.Note;
import com.gotprint.notes.dto.ApiResponse;
import com.gotprint.notes.service.NoteService;
import com.gotprint.notes.util.ApiResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    private final SecurityConfig securityConfig;

    @PostMapping
    public ResponseEntity<Mono<ApiResponse>> createNote(@Valid @RequestBody Note note, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ApiResponseUtil.errorResponse(errors));
        }
        return ResponseEntity.ok(noteService.createNote(note, securityConfig.getLoggedInUserDetails().getUsername()));
    }

    @GetMapping
    public ResponseEntity<Flux<Note>> getAllNotes() {
        return ResponseEntity.ok(noteService.getAllNotes(securityConfig.getLoggedInUserDetails().getUsername()));
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<Mono<Note>> getNote(@PathVariable Long noteId) {
        return ResponseEntity.ok(noteService.getNote(noteId, securityConfig.getLoggedInUserDetails().getUsername()));
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<Mono<ApiResponse>> updateNote(@PathVariable Long noteId, @Valid @RequestBody Note note, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ApiResponseUtil.errorResponse(errors));
        }
        return ResponseEntity.ok(noteService.updateNote(noteId, note, securityConfig.getLoggedInUserDetails().getUsername()));
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Mono<ApiResponse>> deleteNote(@PathVariable Long noteId) {
        return ResponseEntity.ok(noteService.deleteNote(noteId, securityConfig.getLoggedInUserDetails().getUsername()));
    }
    
}
