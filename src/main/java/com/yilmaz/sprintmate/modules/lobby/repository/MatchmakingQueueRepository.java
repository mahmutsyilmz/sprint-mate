package com.yilmaz.sprintmate.modules.lobby.repository;

import com.yilmaz.sprintmate.modules.lobby.entity.MatchmakingQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatchmakingQueueRepository extends JpaRepository<MatchmakingQueue, UUID> {

    // FIFO sırasına göre role bazlı kuyruk
    List<MatchmakingQueue> findByRoleOrderByJoinedAtAsc(String role);

    // Kuyruktaki toplam kişi sayısı (role bazlı)
    long countByRole(String role);

    // Kullanıcı zaten kuyrukta mı?
    boolean existsByUserId(UUID userId);

    // Kullanıcıyı kuyruktan çıkar
    void deleteByUserId(UUID userId);
}
