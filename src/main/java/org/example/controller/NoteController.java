package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.NoteRequest;
import org.example.dto.NoteResponse;
import org.example.service.NoteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@Tag(name = "Notes", description = "Operations related to Notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @Operation(summary = "Get all notes")
    @GetMapping
    public ResponseEntity<List<NoteResponse>> getNotes(Authentication auth) {
        return ResponseEntity.ok(noteService.getUserNotes(auth.getName()));
    }

    @Operation(summary = "Create a new Note")
    @PostMapping
    public ResponseEntity<NoteResponse> createNote(@RequestBody NoteRequest request, Authentication auth) {
        return ResponseEntity.ok(noteService.createNote(auth.getName(), request));
    }

    @Operation(summary = "Update existing note by id")
    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> updateNote(@PathVariable Long id,
                                                   @RequestBody NoteRequest request,
                                                   Authentication auth) {
        return ResponseEntity.ok(noteService.updateNote(auth.getName(), id, request));
    }

    @Operation(summary = "Delete the note")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id, Authentication auth) {
        noteService.deleteNote(auth.getName(), id);
        return ResponseEntity.noContent().build();
    }
}

