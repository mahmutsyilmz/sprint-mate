package com.yilmaz.sprintmate.modules.board.enums;

/**
 * Task Status Enum
 * 
 * Represents the lifecycle states of a task in the Kanban board.
 */
public enum TaskStatus {
    TODO, // Task is in backlog/to-do list
    IN_PROGRESS, // Task is currently being worked on
    REVIEW, // Task is finished and waiting for code review (requires PR)
    DONE // Task is completed and merged
}
