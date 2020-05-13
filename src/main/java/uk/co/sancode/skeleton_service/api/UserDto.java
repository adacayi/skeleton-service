package uk.co.sancode.skeleton_service.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDate;
import java.util.UUID;

public class UserDto {
    private UUID id;
    private String name;
    private String lastName;
    private LocalDate dateOfBirth;

    public UserDto() {

    }

    public UserDto(final UUID id, final String name, final String lastName, final LocalDate dateOfBirth) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public UUID getId() {
        return id;
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
                .append(this.id)
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
                .append(this.id, other.id)
                .append(this.name, other.name)
                .append(this.lastName, other.lastName)
                .append(this.dateOfBirth, other.dateOfBirth)
                .build();
    }
}
