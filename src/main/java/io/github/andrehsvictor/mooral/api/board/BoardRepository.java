package io.github.andrehsvictor.mooral.api.board;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, UUID> {

}
