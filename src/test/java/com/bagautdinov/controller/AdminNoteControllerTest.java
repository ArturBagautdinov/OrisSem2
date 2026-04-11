package com.bagautdinov.controller;

import com.bagautdinov.dto.AdminNoteDto;
import com.bagautdinov.model.Note;
import com.bagautdinov.model.User;
import com.bagautdinov.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminNoteControllerTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private AdminNoteController adminNoteController;

    @Test
    void getAllNotesMapsEntitiesToDtos() {
        when(noteRepository.findAllWithAuthor()).thenReturn(List.of(createNote()));

        List<AdminNoteDto> result = adminNoteController.getAllNotes();

        assertEquals(1, result.size());
        assertEquals("Important", result.get(0).getTitle());
        assertEquals("ivan", result.get(0).getAuthorUsername());
    }

    @Test
    void deleteAnyNoteReturnsNotFoundForMissingNote() {
        when(noteRepository.existsById(1L)).thenReturn(false);

        ResponseEntity<Void> response = adminNoteController.deleteAnyNote(1L);

        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
    }

    @Test
    void deleteAnyNoteDeletesExistingNote() {
        when(noteRepository.existsById(2L)).thenReturn(true);

        ResponseEntity<Void> response = adminNoteController.deleteAnyNote(2L);

        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
        verify(noteRepository).deleteById(2L);
    }

    private Note createNote() {
        User user = new User();
        user.setUsername("ivan");

        Note note = new Note();
        note.setId(1L);
        note.setTitle("Important");
        note.setContent("content");
        note.setCreatedAt(LocalDateTime.of(2026, 4, 11, 12, 0));
        note.setPublic(true);
        note.setAuthor(user);
        return note;
    }
}
