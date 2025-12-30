package com.yilmaz.sprintmate.modules.board.repository;

import com.yilmaz.sprintmate.modules.board.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByTeamId(UUID teamId);
}
