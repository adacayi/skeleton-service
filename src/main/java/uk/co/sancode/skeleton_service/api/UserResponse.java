package uk.co.sancode.skeleton_service.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

public class UserResponse {
    private UUID userId;

    public UserResponse(final UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.userId)
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
                .build();
    }
}
