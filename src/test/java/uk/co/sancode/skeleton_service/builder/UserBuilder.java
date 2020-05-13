package uk.co.sancode.skeleton_service.builder;

import org.apache.commons.lang3.RandomStringUtils;
import uk.co.sancode.skeleton_service.model.User;

import java.time.LocalDate;
import java.util.UUID;

public class UserBuilder {
    private UUID id;
    private String name;
    private String lastName;
    private LocalDate dateOfBirth;

    public UserBuilder() {
        id = UUID.randomUUID();
        name = RandomStringUtils.randomAlphanumeric(getRandomInt(5, 20));
        lastName = RandomStringUtils.randomAlphanumeric(getRandomInt(5, 20));
        dateOfBirth = LocalDate.now().minusYears(getRandomInt(10, 60)).minusDays(getRandomInt(0, 365));
    }

    public UserBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public UserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder withDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public User build() {
        return new User(id, name, lastName, dateOfBirth);
    }

    private int getRandomInt(int startInclusive, int endInclusive) {
        return (int) (Math.random() * (endInclusive - startInclusive + 1)) + startInclusive;
    }
}
