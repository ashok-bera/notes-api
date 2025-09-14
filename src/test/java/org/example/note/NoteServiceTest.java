package org.example.note;


import org.example.dto.NoteRequest;
import org.example.dto.NoteResponse;
import org.example.model.Note;
import org.example.model.User;
import org.example.repo.NoteRepository;
import org.example.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateNote() {
        Note note = new Note();
        User user = new User();
        user.setEmail("manu@2000");
        note.setUser(user);
        note.setTitle("new bro");
        note.setContent("wowo aweos ebro");
        when(noteRepository.save(any(Note.class))).thenReturn(note);

        NoteRequest noteRequest = new NoteRequest();
        noteRequest.setTitle("new bro");
        noteRequest.setContent("wowo aweos ebro");

        NoteResponse savedNote = noteService.createNote("manu@2000", noteRequest);

        assertEquals("Title", savedNote.getTitle());
        verify(noteRepository, times(1)).save(any(Note.class));
    }

}

