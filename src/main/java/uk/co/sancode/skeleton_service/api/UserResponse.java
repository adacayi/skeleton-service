package uk.co.sancode.skeleton_service.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

public class UserResponse {
    private UUID userId;
    private String path;

    public UserResponse() {
    }

    public UserResponse(final UUID userId, final String path) {
        this.userId = userId;
        this.path = path;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.userId)
                .append(this.path)
                .build();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof UserResponse)) {
            return false;
        }

        var other = (UserResponse) obj;
        return new EqualsBuilder()
                .append(this.userId, other.userId)
                .append(this.path, other.path)
                .build();
    }
}
