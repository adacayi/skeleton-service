package uk.co.sancode.skeleton_service.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void get_returnsUserList() throws Exception {
        // Setup

        var users = new ArrayList<User>();
        IntStream.range(0, 2).forEach(x -> users.add(new UserBuilder().build()));

        userRepository.saveAll(users);

        // Exercise

        var response = mockMvc
                .perform(get(baseUrl))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Verify

        var actualDto = objectMapper.readValue(response, UserDto[].class);
        var actual = modelMapper.map(actualDto, User[].class);
        assertEquals(users, List.of(actual));
    }

    @Test
    public void givenUserExistsWithTheId_get_id_returnsUserWithId() throws Exception {
        // Setup

        var users = new ArrayList<User>();
        IntStream.range(0, 4).forEach(x -> users.add(new UserBuilder().build()));
        userRepository.saveAll(users);
        var user = users.get(2);

        // Exercise

        String uri = UriComponentsBuilder.fromPath(baseUrl).pathSegment(user.getId().toString()).build().toUriString();
        var response = mockMvc
                .perform(get(uri))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Verify

        var actualDto = objectMapper.readValue(response, User.class);
        var actual = modelMapper.map(actualDto, User.class);
        assertEquals(user, actual);
    }
}
