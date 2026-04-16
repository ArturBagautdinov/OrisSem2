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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        assertEquals("Мои заметки", model.get("pageTitle"));
        assertEquals(false, model.get("isPublicPage"));
    }

    @Test
    void myNotesLoadsAllAuthorNotesWhenQueryIsBlank() {
        User user = createUser("ivan");
        Note note = createNote(2L, "JPA");
        ExtendedModelMap model = new ExtendedModelMap();

        when(userRepository.findByUsername("ivan")).thenReturn(Optional.of(user));
        when(noteRepository.findByAuthor(user)).thenReturn(List.of(note));

        String viewName = noteController.myNotes("   ", principal("ivan"), model);

        assertEquals("notes", viewName);
        assertEquals(List.of(note), model.get("notes"));
        assertEquals("   ", model.get("query"));
    }

    @Test
    void publicNotesUsesSearchWhenQueryIsPresent() {
        Note note = createNote(3L, "Public");
        ExtendedModelMap model = new ExtendedModelMap();
        when(noteRepository.searchPublic("pub")).thenReturn(List.of(note));

        String viewName = noteController.publicNotes("pub", model);

        assertEquals("public_notes", viewName);
        assertEquals(List.of(note), model.get("notes"));
        assertEquals("pub", model.get("query"));
    }

    @Test
    void publicNotesLoadsPublicNotesWhenQueryIsAbsent() {
        Note note = createNote(4L, "Shared");
        ExtendedModelMap model = new ExtendedModelMap();
        when(noteRepository.findByIsPublicTrue()).thenReturn(List.of(note));

        String viewName = noteController.publicNotes(null, model);

        assertEquals("public_notes", viewName);
        assertEquals(List.of(note), model.get("notes"));
        assertNull(model.get("query"));
    }

    @Test
    void createFormPreparesModel() {
        ExtendedModelMap model = new ExtendedModelMap();

        String viewName = noteController.createForm(model);

        assertEquals("note_form", viewName);
        assertNotNull(model.get("note"));
        assertEquals("/notes/create", model.get("formAction"));
        assertEquals("Создание заметки", model.get("pageTitle"));
    }

    @Test
    void createAssignsCurrentUserAndSavesNote() {
        User user = createUser("ivan");
        Note note = new Note();
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        when(userRepository.findByUsername("ivan")).thenReturn(Optional.of(user));

        String viewName = noteController.create(note, principal("ivan"), redirectAttributes);

        assertEquals("redirect:/notes", viewName);
        assertNull(note.getId());
        assertEquals(user, note.getAuthor());
        assertNotNull(note.getCreatedAt());
        assertEquals("Заметка создана", redirectAttributes.getFlashAttributes().get("successMessage"));
        verify(noteRepository).save(note);
    }

    @Test
    void editFormLoadsOwnedNote() {
        User user = createUser("ivan");
        Note note = createNote(7L, "Editable");
        ExtendedModelMap model = new ExtendedModelMap();

        when(userRepository.findByUsername("ivan")).thenReturn(Optional.of(user));
        when(noteRepository.findByIdAndAuthor(7L, user)).thenReturn(Optional.of(note));

        String viewName = noteController.editForm(7L, principal("ivan"), model);

        assertEquals("note_form", viewName);
        assertSame(note, model.get("note"));
        assertEquals("/notes/7/edit", model.get("formAction"));
        assertEquals("Редактирование заметки", model.get("pageTitle"));
    }

    @Test
    void editFormThrowsWhenNoteBelongsToAnotherUser() {
        User user = createUser("ivan");
        when(userRepository.findByUsername("ivan")).thenReturn(Optional.of(user));
        when(noteRepository.findByIdAndAuthor(8L, user)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> noteController.editForm(8L, principal("ivan"), new ExtendedModelMap()));

        assertEquals("Нельзя редактировать чужую заметку", exception.getMessage());
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
        assertEquals("Заметка обновлена", redirectAttributes.getFlashAttributes().get("successMessage"));
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
        assertEquals("Заметка удалена", redirectAttributes.getFlashAttributes().get("successMessage"));
        verify(noteRepository).delete(storedNote);
    }

    @Test
    void deleteThrowsWhenUserIsMissing() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> noteController.delete(11L, principal("ghost"), new RedirectAttributesModelMap()));

        assertEquals("Пользователь не найден", exception.getMessage());
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
