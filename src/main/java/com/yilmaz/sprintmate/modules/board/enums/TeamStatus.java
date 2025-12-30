package com.yilmaz.sprintmate.modules.board.enums;

/**
 * Team Status Enum
 * 
 * Represents the lifecycle states of a team and their sprint.
 */
public enum TeamStatus {
    LOBBY, // Team is forming (waiting for members)
    GENERATING_PROJECT, // Team is matched, AI is generating the project
    IN_SPRINT, // Active sprint is running
    COMPLETED, // Sprint finished successfully
    ABANDONED, // Sprint cancelled (e.g. member left)
    KICKED // Only for individual member status, but maybe kept here for reference
}
