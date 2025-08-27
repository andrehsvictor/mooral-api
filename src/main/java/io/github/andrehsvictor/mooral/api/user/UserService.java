package io.github.andrehsvictor.mooral.api.user;

import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.andrehsvictor.mooral.api.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "ID", id));
    }

    public User persist(User user) {
        return userRepository.save(user);
    }

    public void delete(UUID id) {
        userRepository.deleteById(id);
    }
}