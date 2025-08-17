package io.github.andrehsvictor.mooral.api.session;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Session, UUID> {

}
