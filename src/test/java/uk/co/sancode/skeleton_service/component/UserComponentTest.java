package uk.co.sancode.skeleton_service.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;
import uk.co.sancode.skeleton_service.api.UserDto;
import uk.co.sancode.skeleton_service.builder.UserBuilder;
import uk.co.sancode.skeleton_service.integration.persistance.UserRepository;
import uk.co.sancode.skeleton_service.model.User;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.co.sancode.skeleton_service.utilities.RandomUtilities.getRandomInt;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles({"mockdatabase"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserComponentTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    private final String baseUrl = "/users";

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

    @Test
    public void givenUserExistsWithTheId_geUser_returnsUserWithId() throws Exception {
        // Setup

        var users = IntStream.range(0, 4).mapToObj(x -> new UserBuilder().build()).collect(Collectors.toList());
        userRepository.saveAll(users);
        var user = users.get(2);

        // Exercise

        var uri = UriComponentsBuilder.fromPath(baseUrl).pathSegment(user.getId().toString()).build().toUriString();
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
}
