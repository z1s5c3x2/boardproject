package com.application.common.domain.dto.boardService;

import com.application.common.domain.entity.boardService.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDto {
    private long boardNo;
    private String boardTitle;
    private String boardContent;
    private String boardWriter;
    private LocalDateTime postDate;
    private LocalDateTime modifyDate;
    public BoardEntity allToEntity(){
        return BoardEntity.builder()
                .boardContent(this.boardContent)
                .boardNo(this.boardNo)
                .boardTitle(this.boardTitle).build();
    }
}
