package uk.co.sancode.skeleton_service.api;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.sancode.skeleton_service.builder.UserResponseBuilder;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

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
    @Parameters(method = "getUserResponses")
    public void equals_returnsCorrectValue(UserResponse firstResponse, UserResponse secondResponse, boolean expected) {
        // Setup

        // Exercise

        var actual = firstResponse.equals(secondResponse);

        // Verify

        assertEquals(expected, actual);
    }

    @Test
    public void givenObjectsEqual_hashCode_returnsSameValue() {
        // Setup

        var user1 = new UserResponseBuilder().build();
        var user2 = new UserResponseBuilder().withUserId(user1.getUserId()).withPath(user1.getPath()).build();

        // Exercise

        var hash1 = user1.hashCode();
        var hash2 = user2.hashCode();

        // Verify

        assertEquals(hash1, hash2);
    }

    @Test
    public void givenObjectsNotEqual_hashCode_returnsDifferentValue() {
        // Setup

        var user1 = new UserResponseBuilder().build();
        var user2 = new UserResponseBuilder().build();

        // Exercise

        var hash1 = user1.hashCode();
        var hash2 = user2.hashCode();

        // Verify

        assertNotEquals(hash1, hash2);
    }

    public List<List<Object>> getUserResponses() {
        var uuid1 = UUID.randomUUID();
        var uuid2 = UUID.randomUUID();

        return List.of(
                List.of(new UserResponseBuilder().withUserId(uuid1).withPath("a").build(), new UserResponseBuilder().withUserId(uuid1).withPath("a").build(), true),
                List.of(new UserResponseBuilder().withUserId(uuid2).withPath("a").build(), new UserResponseBuilder().withUserId(uuid1).withPath("a").build(), false),
                List.of(new UserResponseBuilder().withUserId(uuid1).withPath("b").build(), new UserResponseBuilder().withUserId(uuid1).withPath("a").build(), false)
                );
    }
}
