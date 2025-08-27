package io.github.andrehsvictor.mooral.api.user;

import org.springframework.stereotype.Service;

import io.github.andrehsvictor.mooral.api.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    public Authority getByName(String name) {
        return authorityRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Authority", "name", name));
    }

}
