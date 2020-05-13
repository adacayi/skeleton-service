package uk.co.sancode.skeleton_service.service;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.co.sancode.skeleton_service.builder.UserBuilder;
import uk.co.sancode.skeleton_service.integration.persistance.UserRepository;
import uk.co.sancode.skeleton_service.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public class UserServiceTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private Page<User> mockUserPage;

    List<User> dummyUsers = new ArrayList<>();

    User dummyUser = new UserBuilder().build();

    @Before
    public void init() {
        IntStream.range(0, 4).forEach(x -> dummyUsers.add(new UserBuilder().build()));
        when(mockUserPage.getContent()).thenReturn(dummyUsers);
        when(mockUserRepository.findAll(any(Pageable.class))).thenReturn(mockUserPage);
        when(mockUserRepository.findById(any())).thenReturn(Optional.empty());
        when(mockUserRepository.findById(dummyUser.getId())).thenReturn(Optional.of(dummyUser));
    }

    @Test
    @Parameters({"3", "5"})
    public void getUsers_returnsUsers(int page) {
        // Setup

        var sut = generateSut();
        var size = 10;


        // Exercise

        var actual = sut.getUsers(page, size);

        // Verify

        page--;
        var argument = ArgumentCaptor.forClass(Pageable.class);
        verify(mockUserRepository, times(1)).findAll(argument.capture());
        assertEquals(page, argument.getValue().getPageNumber());
        assertEquals(size, argument.getValue().getPageSize());
        assertEquals(dummyUsers, actual);
    }

    @Test
    public void getUser_returnsUser() {
        // Setup

        var sut = generateSut();

        // Exercise

        var actual = sut.getUser(dummyUser.getId());

        // Verify

        verify(mockUserRepository, times(1)).findById(dummyUser.getId());
        assertTrue(actual.isPresent());
        assertEquals(dummyUser, actual.get());
    }

    public UserService generateSut() {
        return new UserService(mockUserRepository);
    }
}
