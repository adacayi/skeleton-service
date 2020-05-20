package uk.co.sancode.skeleton_service.api;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.sancode.skeleton_service.builder.UserResponseBuilder;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(JUnitParamsRunner.class)
public class UserResponseTest {
    @Test
    public void givenDifferentType_equals_returnsFalse() {
        // Setup

        var sut = new UserResponseBuilder().build();

        // Exercise

        var actual = sut.equals("something");

        // Verify

        assertFalse(actual);
    }

    @Test
    @Parameters(method = "getUsers")
    public void equals_returnsCorrectValue(UserResponse firstUser, UserResponse secondUser, boolean expected) {
        // Setup

        // Exercise

        var actual = firstUser.equals(secondUser);

        // Verify

        assertEquals(expected, actual);
    }

    @Test
    public void givenObjectsEqual_hashCode_returnsSameValue() {
        // Setup

        var user1 = new UserResponseBuilder().build();
        var user2 = new UserResponseBuilder().withUserId(user1.getUserId()).build();

        // Exercise

        var hash1 = user1.hashCode();
        var hash2 = user2.hashCode();

        // Verify

        assertEquals(hash1, hash2);
    }

    public List<List<Object>> getUsers() {
        var uuid1 = UUID.randomUUID();
        var uuid2 = UUID.randomUUID();

        return List.of(
                List.of(new UserResponseBuilder().withUserId(uuid1).build(), new UserResponseBuilder().withUserId(uuid1).build(), true),
                List.of(new UserResponseBuilder().withUserId(uuid2).build(), new UserResponseBuilder().withUserId(uuid1).build(), false)
        );
    }
}
