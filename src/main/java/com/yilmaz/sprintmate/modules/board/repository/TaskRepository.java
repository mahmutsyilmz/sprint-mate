package com.yilmaz.sprintmate.modules.board.repository;

import com.yilmaz.sprintmate.modules.board.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    // Board için: Takımın tüm taskları statüye göre sıralı
    List<Task> findByTeamIdOrderByStatusAscColumnOrderAsc(UUID teamId);

    // Belirli statüdeki tasklar
    List<Task> findByTeamIdAndStatus(UUID teamId, String status);

    // Kullanıcıya atanmış tasklar
    List<Task> findByAssignedUserId(UUID userId);

    // Takımdaki toplam task sayısı
    long countByTeamId(UUID teamId);

    // Tamamlanan task sayısı (XP hesaplaması için)
    long countByTeamIdAndStatus(UUID teamId, String status);
}
