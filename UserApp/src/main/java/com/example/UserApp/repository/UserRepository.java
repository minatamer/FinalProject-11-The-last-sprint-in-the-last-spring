package com.example.UserApp.repository;

import com.example.UserApp.model.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.query.Procedure;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query(value = "SELECT * FROM \"users\"", nativeQuery = true)
    List<User> findAllUsers();

    @Modifying
    @Transactional
    @Query(value = "UPDATE \"users\" SET username=:username, email=:email, age=:age, phone_number=:phoneNumber, gender=:gender WHERE id=:userId", nativeQuery = true)
    void updateUser(@Param("username") String username,
                    @Param("email") String email,
                    @Param("age") int age,
                    @Param("phoneNumber") String phoneNumber,
                    @Param("gender") String gender,
                    @Param("userId") UUID userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.id = :userId")
    void deleteUser(@Param("userId") UUID userId);

    User findByEmail(String email);
    Optional<User> findByToken(String token);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}