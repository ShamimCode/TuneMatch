package com.tunematch.backend.repository;

import com.tunematch.backend.model.UserInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInteractionRepository extends JpaRepository<UserInteraction, Long> {
    List<UserInteraction> findByUser_UserId(Long userId);

    List<UserInteraction> findByUser_UserIdAndInteractionType(Long userId, com.tunematch.backend.model.InteractionType type);

}
