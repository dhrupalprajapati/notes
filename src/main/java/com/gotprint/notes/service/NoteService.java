package com.gotprint.notes.service;

import com.gotprint.notes.domain.Note;
import com.gotprint.notes.dto.ApiResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NoteService {
    Mono<ApiResponse> createNote(Note note, String email);

    Flux<Note> getAllNotes(String email);

    Mono<Note> getNote(Long noteId, String email);

    Mono<ApiResponse> updateNote(Long noteId, Note updatedNote, String email);

    Mono<ApiResponse> deleteNote(Long noteId, String email);
}
