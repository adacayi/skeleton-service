package uk.co.sancode.skeleton_service.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class User {
    @Id
    @Column(name = "user_id")
    private UUID id;
    private String name;
    private String lastName;
    private LocalDate dateOfBirth;

    public User() {

    }

    public User(final UUID id, final String name, final String lastName, final LocalDate dateOfBirth) {
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
        if (!(obj instanceof User)) {
            return false;
        }

        var other = (User) obj;
        return new EqualsBuilder()
                .append(this.id, other.id)
                .append(this.name, other.name)
                .append(this.lastName, other.lastName)
                .append(this.dateOfBirth, other.dateOfBirth)
                .build();
    }
}
