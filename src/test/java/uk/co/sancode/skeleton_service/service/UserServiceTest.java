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
import uk.co.sancode.skeleton_service.controller.DuplicateRecordException;
import uk.co.sancode.skeleton_service.controller.RecordNotFoundException;
import uk.co.sancode.skeleton_service.integration.persistance.UserRepository;
import uk.co.sancode.skeleton_service.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
        when(mockUserRepository.findById(dummyUser.getUserId())).thenReturn(Optional.of(dummyUser));
    }

    // region getUsers

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

    // endregion

    // region getUser

    @Test
    public void givenUserExists_getUser_returnsUser() {
        // Setup

        var sut = generateSut();

        // Exercise

        var actual = sut.getUser(dummyUser.getUserId());

        // Verify

        verify(mockUserRepository, times(1)).findById(dummyUser.getUserId());
        assertTrue(actual.isPresent());
        assertEquals(dummyUser, actual.get());
    }

    // endregion

    // region saveUser

    @Test
    public void givenUserNotExists_saveUser_savesUser() throws DuplicateRecordException {
        // Setup

        var sut = generateSut();
        var user = new UserBuilder().build();

        // Exercise

        sut.saveUser(user);

        // Verify

        verify(mockUserRepository, times(1)).findById(user.getUserId());
        verify(mockUserRepository, times(1)).save(user);
    }

    @Test(expected = DuplicateRecordException.class)
    public void givenUserExists_saveUser_throwsDuplicateRecordException() throws DuplicateRecordException {
        // Setup

        var sut = generateSut();
        var user = new UserBuilder().build();
        when(mockUserRepository.findById(user.getUserId())).thenReturn(Optional.of(mock(User.class)));

        // Exercise

        try {
            sut.saveUser(user);
        } catch (DuplicateRecordException ex) {
            // Verify

            verify(mockUserRepository, times(1)).findById(user.getUserId());
            verify(mockUserRepository, never()).save(any());

            throw ex;
        }
    }

    // endregion

    // region updateUser

    @Test
    public void givenUserExists_updateUser_updatesUser() throws RecordNotFoundException {
        // Setup

        var sut = generateSut();
        var user = new UserBuilder().build();
        when(mockUserRepository.findById(user.getUserId())).thenReturn(Optional.of(mock(User.class)));

        // Exercise

        sut.updateUser(user);

        // Verify

        verify(mockUserRepository, times(1)).findById(user.getUserId());
        verify(mockUserRepository, times(1)).save(user);
    }

    @Test(expected = RecordNotFoundException.class)
    public void givenUserNotExists_updateUser_throwsRecordNotFoundException() throws RecordNotFoundException {
        // Setup

        var sut = generateSut();
        var user = new UserBuilder().build();

        // Exercise
        try {
            sut.updateUser(user);
        } catch (RecordNotFoundException ex) {
            // Verify

            verify(mockUserRepository, times(1)).findById(user.getUserId());
            verify(mockUserRepository, never()).save(any());

            throw ex;
        }
    }

    // endregion

    // region deleteUser

    @Test
    public void givenUserExists_deleteUser_deletesUser() throws RecordNotFoundException {
        // Setup

        var sut = generateSut();
        var id = UUID.randomUUID();
        when(mockUserRepository.findById(id)).thenReturn(Optional.of(mock(User.class)));

        // Exercise

        sut.deleteUser(id);

        // Verify

        verify(mockUserRepository, times(1)).findById(id);
        verify(mockUserRepository, times(1)).deleteById(id);
    }

    @Test(expected = RecordNotFoundException.class)
    public void givenUserNotExists_deleteUser_throwsRecordNotFoundException() throws RecordNotFoundException {
        // Setup

        var sut = generateSut();
        var id = UUID.randomUUID();

        // Exercise
        try {
            sut.deleteUser(id);
        } catch (RecordNotFoundException ex) {
            // Verify

            verify(mockUserRepository, times(1)).findById(id);
            verify(mockUserRepository, never()).deleteById(any());

            throw ex;
        }
    }

    // endregion

    public UserService generateSut() {
        return new UserService(mockUserRepository);
    }
}
