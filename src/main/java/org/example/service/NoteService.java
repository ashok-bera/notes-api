package org.example.service;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.example.dto.NoteRequest;
import org.example.dto.NoteResponse;
import org.example.model.Note;
import org.example.model.User;
import org.example.repo.NoteRepository;
import org.example.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final MeterRegistry meterRegistry;

    public List<NoteResponse> getUserNotes(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return noteRepository.findByUser(user)
                .stream()
                .map(n -> new NoteResponse(n.getId(), n.getTitle(), n.getContent(), n.getCreatedAt(), n.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    public NoteResponse createNote(String email, NoteRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Note note = new Note();
        note.setUser(user);
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        Note saved = noteRepository.save(note);

        // aading custom metrics
        meterRegistry.counter("notes_created_total", "user", email).increment();
        return new NoteResponse(saved.getId(), saved.getTitle(), saved.getContent(), saved.getCreatedAt(), saved.getUpdatedAt());
    }

    public NoteResponse updateNote(String email, Long id, NoteRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Note note = noteRepository.findByIdAndUser(id, user).orElseThrow(
                () -> new RuntimeException("Note not found or not owned by user")
        );
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        Note updated = noteRepository.save(note);
        return new NoteResponse(updated.getId(), updated.getTitle(), updated.getContent(), updated.getCreatedAt(), updated.getUpdatedAt());
    }

    public void deleteNote(String email, Long id) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Note note = noteRepository.findByIdAndUser(id, user).orElseThrow(
                () -> new RuntimeException("Note not found or not owned by user")
        );
        noteRepository.delete(note);
    }
}
