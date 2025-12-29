package com.yilmaz.sprintmate.modules.board.repository;

import com.yilmaz.sprintmate.modules.board.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {

    List<Team> findByStatus(String status);

    List<Team> findByStatusIn(List<String> statuses);
}
