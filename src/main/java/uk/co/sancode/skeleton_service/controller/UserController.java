package uk.co.sancode.skeleton_service.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.sancode.skeleton_service.api.UserDto;
import uk.co.sancode.skeleton_service.integration.persistance.UserRepository;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserController(@Autowired final UserRepository userRepository,
                          @Autowired final ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<UserDto> get() {
        var users = userRepository.findAll();
        Type type = new TypeToken<List<UserDto>>() {
        }.getType();
        return modelMapper.map(users, type);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable(required = false) final UUID id) {
        var user = userRepository.findById(id).orElse(null);
        return modelMapper.map(user, UserDto.class);
    }
}
