package com.bagautdinov.repository;


import com.bagautdinov.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    void deleteByUsername(String username);

    List<User> findAllByOrderByIdAsc();

    @Query("select u from User u where u.username = :username")
    Optional<User> getByUsername(@Param("username") String username);

    @Query(value = "select * from users u where u.username = ?1", nativeQuery = true)
    Optional<User> getByUsernameNative(String username);

    @Modifying
    @Query("update User u set u.username = :username where u.id = :id")
    int updateUsernameById(@Param("id") Long id, @Param("username") String username);
}
