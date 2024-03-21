package com.application.common.domain.entity.replyService;

import com.application.common.domain.entity.boardService.BoardEntity;
import com.application.common.domain.entity.common.BaseTimeEntity;
import com.application.common.domain.entity.userService.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "REPLY")
public class ReplyEntity extends BaseTimeEntity {
    @Id
    @Column(name = "reply_no")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long replyNo;

    @Column(nullable = false)
    private String replyContent;

    @Column(nullable = false,columnDefinition = "bigint default 1",insertable = false)
    private long replyTarget;

    @ManyToOne
    @JoinColumn(name="user_no")
    @ToString.Exclude
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name="board_no")
    @ToString.Exclude
    private BoardEntity boardEntity;

}
