package uk.co.sancode.skeleton_service.builder;

import uk.co.sancode.skeleton_service.api.UserResponse;

import java.util.UUID;

public class UserResponseBuilder {
    private UUID userId;

    public UserResponseBuilder() {
        userId = UUID.randomUUID();
    }

    public UserResponseBuilder withUserId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public UserResponse build() {
        return new UserResponse(userId);
    }
}
