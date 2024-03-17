package com.application.service.board.repository;

import com.application.common.domain.entity.boardService.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardEntityRepository extends JpaRepository<BoardEntity,Long> {
}
