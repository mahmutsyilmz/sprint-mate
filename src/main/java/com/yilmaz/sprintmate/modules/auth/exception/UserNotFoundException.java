package com.yilmaz.sprintmate.modules.auth.exception;

import com.yilmaz.sprintmate.common.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * User Not Found Exception
 */
public class UserNotFoundException extends BaseException {

    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
    }

    public static UserNotFoundException byId(String userId) {
        return new UserNotFoundException("User not found with ID: " + userId);
    }

    public static UserNotFoundException byGithubId(String githubId) {
        return new UserNotFoundException("User not found with GitHub ID: " + githubId);
    }
}
