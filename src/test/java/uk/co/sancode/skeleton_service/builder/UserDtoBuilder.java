package uk.co.sancode.skeleton_service.builder;

import org.apache.commons.lang3.RandomStringUtils;
import uk.co.sancode.skeleton_service.api.UserDto;

import java.time.LocalDate;
import java.util.UUID;

public class UserDtoBuilder {
    private UUID id;
    private String name;
    private String lastName;
    private LocalDate dateOfBirth;

    public UserDtoBuilder() {
        id = UUID.randomUUID();
        name = RandomStringUtils.randomAlphanumeric(getRandomInt(5, 20));
        lastName = RandomStringUtils.randomAlphanumeric(getRandomInt(5, 20));
        dateOfBirth = LocalDate.now().minusYears(getRandomInt(10, 60)).minusDays(getRandomInt(0, 365));
    }

    public UserDtoBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public UserDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserDtoBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserDtoBuilder withDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public UserDto build() {
        return new UserDto(id, name, lastName, dateOfBirth);
    }

    private int getRandomInt(int startInclusive, int endInclusive) {
        return (int) (Math.random() * (endInclusive - startInclusive + 1)) + startInclusive;
    }
}
