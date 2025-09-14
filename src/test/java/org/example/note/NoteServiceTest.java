package org.example.note;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.example.dto.NoteRequest;
import org.example.dto.NoteResponse;
import org.example.model.Note;
import org.example.model.User;
import org.example.repo.NoteRepository;
import org.example.repo.UserRepository;
import org.example.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private Counter counter;

    @InjectMocks
    private NoteService noteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateNote() {
        User user = new User();
        user.setEmail("manu@2000");

        Note note = new Note();
        note.setUser(user);
        note.setTitle("new bro");
        note.setContent("wowo aweos ebro");

        when(userRepository.findByEmail("manu@2000")).thenReturn(Optional.of(user));
        when(noteRepository.save(any(Note.class))).thenReturn(note);
        when(meterRegistry.counter(anyString(), anyString(), anyString())).thenReturn(counter);

        doNothing().when(counter).increment();

        NoteRequest noteRequest = new NoteRequest();
        noteRequest.setTitle("new bro");
        noteRequest.setContent("wowo aweos ebro");

        NoteResponse savedNote = noteService.createNote("manu@2000", noteRequest);

        assertEquals("new bro", savedNote.getTitle());

        verify(userRepository, times(1)).findByEmail("manu@2000");
        verify(noteRepository, times(1)).save(any(Note.class));
        verify(counter, times(1)).increment();
    }
}
