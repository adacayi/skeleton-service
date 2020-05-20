package uk.co.sancode.skeleton_service.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import uk.co.sancode.skeleton_service.api.UserDto;
import uk.co.sancode.skeleton_service.api.UserResponse;
import uk.co.sancode.skeleton_service.model.User;
import uk.co.sancode.skeleton_service.service.UserService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(@Autowired final UserService userService,
                          @Autowired final ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam(defaultValue = "1") final int page,
                                                  @RequestParam(defaultValue = "100") final int size) {
        var users = userService.getUsers(page, size);
        Type type = new TypeToken<List<UserDto>>() {
        }.getType();

        return ResponseEntity.status(OK).body(modelMapper.map(users, type));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable(required = false) final UUID userId) {
        var userResult = userService.getUser(userId);

        return userResult
                .map(user -> ResponseEntity.status(OK).body(modelMapper.map(user, UserDto.class)))
                .orElseGet(() -> ResponseEntity.status(NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<UserResponse> saveUser(@RequestBody final UserDto userDto) throws DuplicateRecordException {
        var userId = userDto.getUserId();
        userService.saveUser(modelMapper.map(userDto, User.class));

        return ResponseEntity.status(CREATED).body(new UserResponse(userId, "/users/" + userDto.getUserId()));
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody final UserDto userDto) throws RecordNotFoundException {
        var userId = userDto.getUserId();
        userService.updateUser(modelMapper.map(userDto, User.class));

        return ResponseEntity.status(OK).body(new UserResponse(userId, "/users/" + userDto.getUserId()));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable final UUID userId) throws RecordNotFoundException {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
