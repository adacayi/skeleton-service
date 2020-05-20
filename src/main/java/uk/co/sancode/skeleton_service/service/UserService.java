package uk.co.sancode.skeleton_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uk.co.sancode.skeleton_service.controller.DuplicateRecordException;
import uk.co.sancode.skeleton_service.controller.RecordNotFoundException;
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

    public Optional<User> getUser(final UUID userId) {
        return userRepository.findById(userId);
    }

    public void saveUser(final User user) throws DuplicateRecordException {
        var userResult = userRepository.findById(user.getUserId());

        if (userResult.isPresent()) {
            throw new DuplicateRecordException();
        }

        userRepository.save(user);
    }

    public void updateUser(final User user) throws RecordNotFoundException {
        var userResult = userRepository.findById(user.getUserId());

        if (userResult.isEmpty()) {
            throw new RecordNotFoundException();
        }

        userRepository.save(user);
    }

    public void deleteUser(final UUID userId) throws RecordNotFoundException {
        var userResult = userRepository.findById(userId);

        if (userResult.isEmpty()) {
            throw new RecordNotFoundException();
        }

        userRepository.deleteById(userId);
    }
}
