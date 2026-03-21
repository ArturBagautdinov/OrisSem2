package com.bagautdinov.dto;

import com.bagautdinov.model.Note;

public class AdminNoteDto {

    private Long id;
    private String title;
    private String content;
    private String createdAt;
    private boolean isPublic;
    private String authorUsername;

    public static AdminNoteDto from(Note note) {
        AdminNoteDto dto = new AdminNoteDto();
        dto.setId(note.getId());
        dto.setTitle(note.getTitle());
        dto.setContent(note.getContent());
        dto.setCreatedAt(note.getCreatedAt() != null
                ? note.getCreatedAt().toString()
                : null);
        dto.setPublic(note.isPublic());
        dto.setAuthorUsername(
                note.getAuthor() != null ? note.getAuthor().getUsername() : null
        );
        return dto;
    }

    // --- getters / setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }
}