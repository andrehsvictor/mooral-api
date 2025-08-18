package io.github.andrehsvictor.mooral.api.mural;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MuralRepository extends JpaRepository<Mural, UUID> {

}
