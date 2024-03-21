package com.application.service.reply.repository;

import com.application.common.domain.entity.replyService.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<ReplyEntity,Long> {
}
