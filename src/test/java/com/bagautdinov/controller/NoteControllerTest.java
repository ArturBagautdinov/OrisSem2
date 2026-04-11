package com.bagautdinov.controller;

import com.bagautdinov.model.Note;
import com.bagautdinov.model.User;
import com.bagautdinov.repository.NoteRepository;
import com.bagautdinov.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoteControllerTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NoteController noteController;

    @Test
    void myNotesUsesSearchWhenQueryIsPresent() {
        User user = createUser("ivan");
        Note note = createNote(1L, "Spring");
        ExtendedModelMap model = new ExtendedModelMap();

        when(userRepository.findByUsername("ivan")).thenReturn(Optional.of(user));
        when(noteRepository.searchByAuthor(user, "spr")).thenReturn(List.of(note));

        String viewName = noteController.myNotes("spr", principal("ivan"), model);

        assertEquals("notes", viewName);
        assertEquals(List.of(note), model.get("notes"));
        assertEquals("spr", model.get("query"));
    }

    @Test
    void publicNotesLoadsPublicNotesWhenQueryIsAbsent() {
        Note note = createNote(2L, "Shared");
        ExtendedModelMap model = new ExtendedModelMap();
        when(noteRepository.findByIsPublicTrue()).thenReturn(List.of(note));

        String viewName = noteController.publicNotes(null, model);

        assertEquals("public_notes", viewName);
        assertEquals(List.of(note), model.get("notes"));
    }

    @Test
    void createAssignsCurrentUserAndSavesNote() {
        User user = createUser("ivan");
        Note note = new Note();
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        when(userRepository.findByUsername("ivan")).thenReturn(Optional.of(user));

        String viewName = noteController.create(note, principal("ivan"), redirectAttributes);

        assertEquals("redirect:/notes", viewName);
        assertEquals(user, note.getAuthor());
        assertNotNull(note.getCreatedAt());
        verify(noteRepository).save(note);
    }

    @Test
    void editUpdatesOwnedNote() {
        User user = createUser("ivan");
        Note storedNote = createNote(9L, "Old");
        Note formNote = createNote(null, "New");
        formNote.setContent("Updated content");
        formNote.setPublic(true);
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        when(userRepository.findByUsername("ivan")).thenReturn(Optional.of(user));
        when(noteRepository.findByIdAndAuthor(9L, user)).thenReturn(Optional.of(storedNote));

        String viewName = noteController.edit(9L, formNote, principal("ivan"), redirectAttributes);

        assertEquals("redirect:/notes", viewName);
        assertEquals("New", storedNote.getTitle());
        assertEquals("Updated content", storedNote.getContent());
        assertEquals(true, storedNote.isPublic());
        verify(noteRepository).save(storedNote);
    }

    @Test
    void deleteRemovesOwnedNote() {
        User user = createUser("ivan");
        Note storedNote = createNote(10L, "Delete me");
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        when(userRepository.findByUsername("ivan")).thenReturn(Optional.of(user));
        when(noteRepository.findByIdAndAuthor(10L, user)).thenReturn(Optional.of(storedNote));

        String viewName = noteController.delete(10L, principal("ivan"), redirectAttributes);

        assertEquals("redirect:/notes", viewName);
        verify(noteRepository).delete(storedNote);
    }

    private Principal principal(String username) {
        return () -> username;
    }

    private User createUser(String username) {
        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword("secret");
        user.setEmail(username + "@mail.com");
        user.setVerified(true);
        return user;
    }

    private Note createNote(Long id, String title) {
        Note note = new Note();
        note.setId(id);
        note.setTitle(title);
        note.setContent(title + " content");
        note.setCreatedAt(LocalDateTime.of(2026, 4, 11, 10, 0));
        note.setPublic(false);
        note.setAuthor(createUser("author"));
        return note;
    }
}
