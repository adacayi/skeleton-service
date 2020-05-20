package uk.co.sancode.skeleton_service.builder;

import uk.co.sancode.skeleton_service.api.UserResponse;

import java.util.UUID;

public class UserResponseBuilder {
    private UUID userId;
    private String path;

    public UserResponseBuilder() {
        userId = UUID.randomUUID();
        path = "/users/" + userId;
    }

    public UserResponseBuilder withUserId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public UserResponseBuilder withPath(String path) {
        this.path = path;
        return this;
    }

    public UserResponse build() {
        return new UserResponse(userId, path);
    }
}
