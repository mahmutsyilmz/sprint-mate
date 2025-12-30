package com.yilmaz.sprintmate.modules.board.service;

import com.yilmaz.sprintmate.modules.ai.dto.ProjectBlueprint;
import com.yilmaz.sprintmate.modules.ai.service.ProjectGenerationService;
import com.yilmaz.sprintmate.modules.auth.entity.User;
import com.yilmaz.sprintmate.modules.board.entity.Task;
import com.yilmaz.sprintmate.modules.board.entity.Team;
import com.yilmaz.sprintmate.modules.board.enums.TaskStatus;
import com.yilmaz.sprintmate.modules.board.enums.TeamStatus;
import com.yilmaz.sprintmate.modules.board.repository.TaskRepository;
import com.yilmaz.sprintmate.modules.board.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Team Service
 * 
 * Manages Team lifecycle and Task creation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {

        private final TeamRepository teamRepository;
        private final TaskRepository taskRepository;
        private final ProjectGenerationService projectGenerationService;
        private final com.yilmaz.sprintmate.modules.auth.repository.UserRepository userRepository;

        /**
         * Create a new team and generate project
         */
        @Transactional
        public Team createTeam(List<User> members) {
                log.info("Creating new team with {} members", members.size());

                // 1. Generate Project from AI
                ProjectBlueprint blueprint = projectGenerationService.generateMockProject();

                // 2. Create Team Entity
                Team team = Team.builder()
                                .name(blueprint.getName())
                                .status(TeamStatus.IN_SPRINT) // Directly start sprint for MVP
                                .aiProjectStory(blueprint.getDescription())
                                .createdAt(LocalDateTime.now())
                                .startDate(LocalDateTime.now())
                                .endDate(LocalDateTime.now().plusDays(7)) // 1 Week Sprint
                                .build();

                team = teamRepository.save(team);

                // 3. Create Tasks
                Team finalTeam = team;
                List<Task> tasks = blueprint.getTasks().stream()
                                .map(t -> Task.builder()
                                                .team(finalTeam)
                                                .title(t.getTitle())
                                                .description(t.getDescription())
                                                .taskType(t.getType())
                                                .difficultyScore(t.getDifficulty().byteValue())
                                                .status(TaskStatus.TODO)
                                                .columnOrder(0)
                                                .build())
                                .collect(Collectors.toList());

                taskRepository.saveAll(tasks);

                // 4. Assign members to team
                for (User member : members) {
                        member.setTeam(team);
                        userRepository.save(member);
                }

                log.info("Team created: {} with {} tasks. Members assigned.", team.getName(), tasks.size());
                return team;
        }

        /**
         * Get Board Data for a User
         */
        @Transactional(readOnly = true)
        public com.yilmaz.sprintmate.modules.board.dto.TeamBoardResponse getTeamBoard(User user) {
                if (user.getTeam() == null) {
                        throw new RuntimeException("User is not in a team"); // Or custom TeamNotFoundException
                }

                Team team = user.getTeam();

                // Fetch tasks
                List<Task> tasks = taskRepository.findByTeamId(team.getId());

                List<User> members = userRepository.findByTeamId(team.getId());

                return com.yilmaz.sprintmate.modules.board.dto.TeamBoardResponse.builder()
                                .teamId(team.getId())
                                .teamName(team.getName())
                                .teamStatus(team.getStatus())
                                .projectStory(team.getAiProjectStory())
                                .sprintEndDate(team.getEndDate())
                                .members(members.stream()
                                                .map(com.yilmaz.sprintmate.modules.auth.dto.UserDto::fromEntity)
                                                .collect(Collectors.toList()))
                                .tasks(tasks.stream().map(this::mapTaskToDto).collect(Collectors.toList()))
                                .build();
        }

        /**
         * Move Task (Update Status)
         */
        @Transactional
        public com.yilmaz.sprintmate.modules.board.dto.TeamBoardResponse moveTask(java.util.UUID taskId,
                        TaskStatus newStatus, User user) {
                Task task = taskRepository.findById(taskId)
                                .orElseThrow(() -> new RuntimeException("Task not found"));

                // Security check: Task must belong to user's team
                if (!task.getTeam().getId().equals(user.getTeam().getId())) {
                        throw new RuntimeException("Access denied");
                }

                log.info("Moving task {} to {}", task.getTitle(), newStatus);

                task.setStatus(newStatus);
                if (newStatus == TaskStatus.IN_PROGRESS && task.getAssignedUser() == null) {
                        task.setAssignedUser(user); // Auto-assign if not assigned
                }

                taskRepository.save(task);

                // Return updated board
                return getTeamBoard(user);
        }

        private com.yilmaz.sprintmate.modules.board.dto.TeamBoardResponse.TaskDto mapTaskToDto(Task task) {
                return com.yilmaz.sprintmate.modules.board.dto.TeamBoardResponse.TaskDto.builder()
                                .id(task.getId())
                                .title(task.getTitle())
                                .description(task.getDescription())
                                .status(task.getStatus())
                                .type(task.getTaskType())
                                .difficultyScore(task.getDifficultyScore())
                                .assignedUserId(task.getAssignedUser() != null ? task.getAssignedUser().getId() : null)
                                .assignedUserAvatar(
                                                task.getAssignedUser() != null ? task.getAssignedUser().getAvatarUrl()
                                                                : null)
                                .build();
        }
}
