package com.yilmaz.sprintmate.modules.ai.repository;

import com.yilmaz.sprintmate.modules.ai.entity.SystemPrompt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemPromptRepository extends JpaRepository<SystemPrompt, Integer> {

    List<SystemPrompt> findByIsActiveTrue();

    List<SystemPrompt> findByCategoryNameAndIsActiveTrue(String categoryName);
}
