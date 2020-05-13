package uk.co.sancode.skeleton_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uk.co.sancode.skeleton_service.integration.persistance.UserRepository;
import uk.co.sancode.skeleton_service.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(@Autowired final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers(final int page, final int size) {
        var result = userRepository.findAll(PageRequest.of(page - 1, size));
        return result.getContent();
    }

    public Optional<User> getUser(final UUID id) {
        return userRepository.findById(id);
    }
}
