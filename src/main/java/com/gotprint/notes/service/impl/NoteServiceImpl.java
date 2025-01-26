package com.gotprint.notes.service.impl;

import com.gotprint.notes.domain.Note;
import com.gotprint.notes.domain.User;
import com.gotprint.notes.dto.ApiResponse;
import com.gotprint.notes.repository.NoteRepository;
import com.gotprint.notes.service.NoteService;
import com.gotprint.notes.service.UserService;
import com.gotprint.notes.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);

    private final UserService userService;

    private final NoteRepository noteRepository;

    public Mono<ApiResponse> createNote(Note note, String email) {
        logger.info("createNote :: received request to create note for email: {}", email);
        return Mono.fromCallable(() -> userService.findByEmail(email))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalUser -> {
                    note.setUser(optionalUser.get());
                    return Mono.fromCallable(() -> noteRepository.save(note))
                            .subscribeOn(Schedulers.boundedElastic());
                })
                .doOnSuccess(saveNote -> logger.info("createNote :: note created successfully for email: {}", email))
                .flatMap(savedNote -> ApiResponseUtil.successResponse("Note created successfully!"))
                .doOnError(error -> logger.error("createNote :: error while creating note: {}", error.getMessage(), error))
                .onErrorResume(error -> ApiResponseUtil.errorResponse("Error while creating note!"));
    }

    public Flux<Note> getAllNotes(String email) {
        logger.info("getAllNotes :: received request to fetch all note for email: {}", email);
        return Flux.defer(() -> {
            Optional<User> optionalUser = userService.findByEmail(email);
            if (optionalUser.isPresent()) {
                List<Note> notes = optionalUser.get().getNotes();
                logger.info("getAllNotes :: total: {} notes found for email: {}", notes.size(), email);
                return Flux.fromIterable(notes);
            } else {
                logger.info("getAllNotes :: no notes found for email: {}", email);
                return Flux.empty();
            }
        });
    }

    public Mono<Note> getNote(Long noteId, String email) {
        logger.info("getNote :: received request to get note for email: {}", email);
        return Mono.fromCallable(() -> userService.findByEmail(email))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalUser -> {
                    if (optionalUser.isEmpty()) {
                        logger.info("getNote :: user not found with given email: {}", email);
                        return Mono.empty();
                    }
                    return Mono.fromCallable(() -> noteRepository.findByIdAndUserId(noteId, optionalUser.get().getId()))
                            .subscribeOn(Schedulers.boundedElastic());
                })
                .flatMap(optionalNote -> {
                    if (optionalNote.isEmpty()) {
                        logger.info("getNote :: note not found with given email: {}, and noteId: {}", email, noteId);
                        return Mono.empty();
                    }
                    logger.info("getNote :: note fetched successfully for email: {}, noteId: {}", email, noteId);
                    return Mono.just(optionalNote.get());
                });
    }

    public Mono<ApiResponse> updateNote(Long noteId, Note updatedNote, String email) {
        logger.info("updateNote :: received request to update note for email: {}, noteId: {}", email, noteId);
        return getNote(noteId, email)
                .flatMap(note -> {
                    note.setTitle(updatedNote.getTitle());
                    note.setNote(updatedNote.getNote());
                    return Mono.fromCallable(() -> noteRepository.save(note))
                            .subscribeOn(Schedulers.boundedElastic());
                })
                .flatMap(note -> {
                    logger.info("updateNote :: note updated successfully for email: {}, noteId: {}", email, noteId);
                    return ApiResponseUtil.successResponse("Note updated successfully!");
                })
                .switchIfEmpty(Mono.defer(() -> {
                    logger.info("updateNote :: note not found with given email: {}, noteId: {}", email, noteId);
                    return ApiResponseUtil.errorResponse("Note not found", HttpStatus.NOT_FOUND);
                }));
    }

    public Mono<ApiResponse> deleteNote(Long noteId, String email) {
        logger.info("deleteNote :: received request to delete note for email: {}, noteId: {}", email, noteId);
        return getNote(noteId, email)
                .flatMap(note -> Mono.fromRunnable(() -> noteRepository.deleteById(noteId))
                        .subscribeOn(Schedulers.boundedElastic())
                        .doOnTerminate(() -> logger.info("deleteNote :: note deleted successfully for email: {}, noteId: {}", email, noteId))
                        .then(ApiResponseUtil.successResponse("Note deleted successfully!")))
                .switchIfEmpty(Mono.defer(() -> {
                    logger.info("deleteNote :: note not found with given email: {}, noteId: {}", email, noteId);
                    return ApiResponseUtil.errorResponse("Note not found", HttpStatus.NOT_FOUND);
                }));
    }

}
