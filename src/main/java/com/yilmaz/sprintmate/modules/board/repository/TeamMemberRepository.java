package com.yilmaz.sprintmate.modules.board.repository;

import com.yilmaz.sprintmate.modules.board.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, UUID> {

    List<TeamMember> findByTeamId(UUID teamId);

    List<TeamMember> findByUserId(UUID userId);

    Optional<TeamMember> findByTeamIdAndUserId(UUID teamId, UUID userId);

    // Ghost Protocol: 24/48 saat inaktif kullanıcıları bul
    @Query("SELECT tm FROM TeamMember tm WHERE tm.status = 'ACTIVE' AND tm.lastActivityAt < :threshold")
    List<TeamMember> findInactiveMembers(@Param("threshold") LocalDateTime threshold);

    // Kullanıcının aktif takımı var mı?
    boolean existsByUserIdAndStatusIn(UUID userId, List<String> statuses);
}
