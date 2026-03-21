package com.bagautdinov.controller;

import com.bagautdinov.dto.AdminNoteDto;
import com.bagautdinov.model.Note;
import com.bagautdinov.repository.NoteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/notes")
public class AdminNoteController {

    private final NoteRepository noteRepository;

    public AdminNoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @GetMapping
    public List<AdminNoteDto> getAllNotes() {
        return noteRepository.findAllWithAuthor().stream()
                .map(AdminNoteDto::from)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnyNote(@PathVariable("id") Long id) {
        if (!noteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        noteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}