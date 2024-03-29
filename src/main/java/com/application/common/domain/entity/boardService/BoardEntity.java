package com.application.common.domain.entity.boardService;

import com.application.common.domain.dto.boardService.BoardDto;
import com.application.common.domain.entity.common.BaseTimeEntity;
import com.application.common.domain.entity.replyService.ReplyEntity;
import com.application.common.domain.entity.userService.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "BOARD")
public class BoardEntity extends BaseTimeEntity {
    @Id
    @Column(name = "board_no")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long boardNo;

    @Column(nullable = false)
    private String boardTitle;
    @Column(nullable = false)
    private String boardContent;

    @ManyToOne
    @JoinColumn(name="user_no")
    @ToString.Exclude
    private UserEntity userEntity;

    @OneToMany(mappedBy = "boardEntity",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Builder.Default
    private List<ReplyEntity> replyEntities = new ArrayList<>();

    public BoardDto allToDto(){
        return BoardDto.builder()
                .boardWriter(this.userEntity.getUserEmail())
                .boardTitle(this.boardTitle)
                .boardContent(this.boardContent)
                .boardNo(this.boardNo)
                .modifyDate(this.getModifyDateTime())
                .postDate(this.getInsertDataTime()).build();
    }
}
