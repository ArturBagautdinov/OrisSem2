package com.bagautdinov.controller;

import com.bagautdinov.model.Note;
import com.bagautdinov.model.User;
import com.bagautdinov.repository.NoteRepository;
import com.bagautdinov.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteController(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String myNotes(@RequestParam(value = "q", required = false) String q,
                          Principal principal,
                          Model model) {
        User currentUser = getCurrentUser(principal);
        List<Note> notes = (q != null && !q.isBlank())
                ? noteRepository.searchByAuthor(currentUser, q)
                : noteRepository.findByAuthor(currentUser);

        model.addAttribute("notes", notes);
        model.addAttribute("query", q);
        model.addAttribute("pageTitle", "Мои заметки");
        model.addAttribute("isPublicPage", false);
        return "notes";
    }

    @GetMapping("/public")
    public String publicNotes(@RequestParam(value = "q", required = false) String q,
                              Model model) {
        List<Note> notes = (q != null && !q.isBlank())
                ? noteRepository.searchPublic(q)
                : noteRepository.findByIsPublicTrue();

        model.addAttribute("notes", notes);
        model.addAttribute("query", q);
        return "public_notes";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("note", new Note());
        model.addAttribute("formAction", "/notes/create");
        model.addAttribute("pageTitle", "Создание заметки");
        return "note_form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("note") Note note,
                         Principal principal,
                         RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(principal);
        note.setId(null);
        note.setAuthor(currentUser);
        note.setCreatedAt(java.time.LocalDateTime.now());
        noteRepository.save(note);
        redirectAttributes.addFlashAttribute("successMessage", "Заметка создана");
        return "redirect:/notes";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id,
                           Principal principal,
                           Model model) {
        User currentUser = getCurrentUser(principal);
        Note note = noteRepository.findByIdAndAuthor(id, currentUser)
                .orElseThrow(() -> new IllegalArgumentException("Нельзя редактировать чужую заметку"));

        model.addAttribute("note", note);
        model.addAttribute("formAction", "/notes/" + id + "/edit");
        model.addAttribute("pageTitle", "Редактирование заметки");
        return "note_form";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id,
                       @ModelAttribute("note") Note formNote,
                       Principal principal,
                       RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(principal);
        Note note = noteRepository.findByIdAndAuthor(id, currentUser)
                .orElseThrow(() -> new IllegalArgumentException("Нельзя редактировать чужую заметку"));

        note.setTitle(formNote.getTitle());
        note.setContent(formNote.getContent());
        note.setPublic(formNote.isPublic());
        noteRepository.save(note);

        redirectAttributes.addFlashAttribute("successMessage", "Заметка обновлена");
        return "redirect:/notes";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id,
                         Principal principal,
                         RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(principal);
        Note note = noteRepository.findByIdAndAuthor(id, currentUser)
                .orElseThrow(() -> new IllegalArgumentException("Нельзя удалить чужую заметку"));

        noteRepository.delete(note);
        redirectAttributes.addFlashAttribute("successMessage", "Заметка удалена");
        return "redirect:/notes";
    }

    private User getCurrentUser(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден"));
    }
}