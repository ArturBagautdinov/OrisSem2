package com.bagautdinov.repository;

import com.bagautdinov.model.Note;
import com.bagautdinov.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByAuthor(User author);

    List<Note> findByIsPublicTrue();

    @Query("select n from Note n where n.author = :author and " +
            "(lower(n.title) like lower(concat('%', :query, '%')) or " +
            "lower(n.content) like lower(concat('%', :query, '%'))) " +
            "order by n.createdAt desc")
    List<Note> searchByAuthor(@Param("author") User author, @Param("query") String query);

    @Query("select n from Note n where n.isPublic = true and " +
            "(lower(n.title) like lower(concat('%', :query, '%')) or " +
            "lower(n.content) like lower(concat('%', :query, '%'))) " +
            "order by n.createdAt desc")
    List<Note> searchPublic(@Param("query") String query);

    @Query("select n from Note n join fetch n.author order by n.createdAt desc")
    List<Note> findAllWithAuthor();

    Optional<Note> findByIdAndAuthor(Long id, User author);
}
