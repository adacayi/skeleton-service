package uk.co.sancode.skeleton_service.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDate;
import java.util.UUID;

public class UserDto {
    private UUID userId;
    private String name;
    private String lastName;
    private LocalDate dateOfBirth;

    public UserDto() {

    }

    public UserDto(final UUID userId, final String name, final String lastName, final LocalDate dateOfBirth) {
        this.userId = userId;
        this.name = name;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.userId)
                .append(this.name)
                .append(this.lastName)
                .append(this.dateOfBirth)
                .build();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof UserDto)) {
            return false;
        }

        var other = (UserDto) obj;
        return new EqualsBuilder()
                .append(this.userId, other.userId)
                .append(this.name, other.name)
                .append(this.lastName, other.lastName)
                .append(this.dateOfBirth, other.dateOfBirth)
                .build();
    }
}
