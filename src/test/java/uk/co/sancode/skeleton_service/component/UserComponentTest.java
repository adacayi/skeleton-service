package uk.co.sancode.skeleton_service.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;
import uk.co.sancode.skeleton_service.api.UserDto;
import uk.co.sancode.skeleton_service.api.UserResponse;
import uk.co.sancode.skeleton_service.builder.UserBuilder;
import uk.co.sancode.skeleton_service.builder.UserDtoBuilder;
import uk.co.sancode.skeleton_service.builder.UserResponseBuilder;
import uk.co.sancode.skeleton_service.integration.persistance.UserRepository;
import uk.co.sancode.skeleton_service.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.co.sancode.skeleton_service.utilities.RandomUtilities.getRandomInt;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(JUnitParamsRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles({"mockdatabase"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserComponentTest {
    @ClassRule
    public static final SpringClassRule SCR = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    private final String baseUrl = "/users";

    // region getUsers

    @Test
    public void givenUsersExist_getUsers_returnsUsers() throws Exception {
        // Setup

        var users = IntStream.range(0, getRandomInt(2, 4)).mapToObj(x -> new UserBuilder().build()).collect(Collectors.toList());
        userRepository.saveAll(users);

        // Exercise

        var response = mockMvc.perform(get(baseUrl))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Verify

        assertNotNull(response);
        var actualDto = objectMapper.readValue(response, UserDto[].class);
        var type = new TypeToken<List<User>>() {
        }.getType();
        var actual = modelMapper.map(actualDto, type);

        assertEquals(users, actual);
    }

    @Test
    public void givenUsersExistAndPageAndSizeGiven_getUsers_returnsUsers() throws Exception {
        // Setup

        var users = IntStream.range(0, 4).mapToObj(x -> new UserBuilder().build()).collect(Collectors.toList());
        userRepository.saveAll(users);

        // Exercise

        var response = mockMvc.perform(get(baseUrl)
                .param("page", "2")
                .param("size", "2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Verify

        assertNotNull(response);
        var actualDto = objectMapper.readValue(response, UserDto[].class);
        var type = new TypeToken<List<User>>() {
        }.getType();
        var actual = modelMapper.map(actualDto, type);
        assertEquals(users.subList(2, 4), actual);
    }

    // endregion

    // region getUser

    @Test
    public void givenUserExistsWithTheId_geUser_returnsUserWithId() throws Exception {
        // Setup

        var users = IntStream.range(0, 4).mapToObj(x -> new UserBuilder().build()).collect(Collectors.toList());
        userRepository.saveAll(users);
        var user = users.get(2);

        // Exercise

        var uri = UriComponentsBuilder.fromPath(baseUrl).pathSegment(user.getUserId().toString()).build().toUriString();
        var response = mockMvc
                .perform(get(uri))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Verify

        var actualDto = objectMapper.readValue(response, UserDto.class);
        var actual = modelMapper.map(actualDto, User.class);
        assertEquals(user, actual);
    }

    @Test
    public void givenUserNotExistsWithTheId_getUser_returns404() throws Exception {
        // Setup

        var users = IntStream.range(0, 4).mapToObj(x -> new UserBuilder().build()).collect(Collectors.toList());
        userRepository.saveAll(users);
        var id = UUID.randomUUID().toString();

        // Exercise

        var uri = UriComponentsBuilder.fromPath(baseUrl).pathSegment(id).build().toUriString();
        mockMvc
                .perform(get(uri))
                .andExpect(status().isNotFound());

        // Verify
    }

    // endregion

    // region saveUser

    @Test
    public void givenUserNotExists_saveUser_savesUserAndReturns201() throws Exception {
        // Setup

        var userDto = new UserDtoBuilder().build();
        var userId = userDto.getUserId();

        // Exercise

        var response = mockMvc
                .perform(post(baseUrl)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Verify

        var expected = new UserResponseBuilder().withUserId(userId).withPath(baseUrl + "/" + userId).build();
        var actual = objectMapper.readValue(response, UserResponse.class);
        assertEquals(expected, actual);
        var userResult = userRepository.findById(userId);
        assertTrue(userResult.isPresent());
        assertEquals(userDto, modelMapper.map(userResult.get(), UserDto.class));
    }

    @Test
    public void givenUserExists_saveUser_returns409() throws Exception {
        // Setup

        var userDto = new UserDtoBuilder().build();
        var user = modelMapper.map(userDto, User.class);
        userRepository.save(user);

        // Exercise

        var errorMessage = mockMvc
                .perform(post(baseUrl)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isConflict())
                .andReturn()
                .getResponse()
                .getErrorMessage();

        // Verify

        assertEquals("A record with this id already exists", errorMessage);
    }

    @Test
    @Parameters(method = "getInvalidUsers")
    public void givenInvalidValues_saveUser_returns404(UserDto userDto) throws Exception {
        // Setup

        // Exercise

        mockMvc.perform(post(baseUrl)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

        // Verify
    }

    // endregion

    // region updateUser

    @Test
    public void givenUserNotExists_updateUser_returns404() throws Exception {
        // Setup

        var userDto = new UserDtoBuilder().build();

        // Exercise

        var errorMessage = mockMvc
                .perform(put(baseUrl)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getErrorMessage();

        // Verify

        assertEquals("A record with this id does not exist", errorMessage);
    }

    @Test
    public void givenUserExists_updateUser_updatesUserAndReturns200() throws Exception {
        // Setup

        var userDto = new UserDtoBuilder().build();
        var userId = userDto.getUserId();
        var user = modelMapper.map(userDto, User.class);
        userRepository.save(user);
        userDto = new UserDtoBuilder().withUserId(userDto.getUserId()).build();

        // Exercise

        var response = mockMvc
                .perform(put(baseUrl)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Verify

        var expected = new UserResponseBuilder().withUserId(userId).withPath(baseUrl + "/" + userId).build();
        var actual = objectMapper.readValue(response, UserResponse.class);
        assertEquals(expected, actual);
        var userResult = userRepository.findById(userId);
        assertTrue(userResult.isPresent());
        assertEquals(userDto, modelMapper.map(userResult.get(), UserDto.class));
    }

    // endregion

    // region deleteUser

    @Test
    public void givenUserNotExists_deleteUser_returns404() throws Exception {
        // Setup

        var userDto = new UserDtoBuilder().build();
        var uri = UriComponentsBuilder.fromPath(baseUrl).pathSegment(userDto.getUserId().toString()).build().toUriString();


        // Exercise

        var errorMessage = mockMvc
                .perform(delete(uri))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getErrorMessage();

        // Verify

        assertEquals("A record with this id does not exist", errorMessage);
    }

    @Test
    public void givenUserExists_deleteUser_deletesUserAndReturns200() throws Exception {
        // Setup

        var userDto = new UserDtoBuilder().build();
        var userId = userDto.getUserId();
        var user = modelMapper.map(userDto, User.class);
        userRepository.save(user);
        var uri = UriComponentsBuilder.fromPath(baseUrl).pathSegment(userDto.getUserId().toString()).build().toUriString();

        // Exercise

        mockMvc
                .perform(delete(uri))
                .andExpect(status().isOk());

        // Verify

        var userResult = userRepository.findById(userId);
        assertTrue(userResult.isEmpty());
    }

    // endregion

    private List<Object> getInvalidUsers() {
        return List.of(
                new UserDtoBuilder().withUserId(null).build(),
                new UserDtoBuilder().withName(null).build(),
                new UserDtoBuilder().withLastName(null).build(),
                new UserDtoBuilder().withDateOfBirth(null).build(),
                new UserDtoBuilder().withName("a").build(),
                new UserDtoBuilder().withLastName("b").build(),
                new UserDtoBuilder().withDateOfBirth(LocalDate.now().plusDays(1)).build()
        );
    }
}
