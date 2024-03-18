package com.application.service.board.controller;

import com.application.common.domain.dto.boardService.BoardDto;
import com.application.common.domain.dto.common.PageDto;
import com.application.service.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@Slf4j
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/list")
    public PageDto<BoardDto> getBoardList(@RequestParam("page") int page,
                                          @RequestParam("size") int size){

        return boardService.getBoardList(page,size);
    }

    @PostMapping("/post")
    public boolean boardWrite(@RequestBody BoardDto boardDto){
        System.out.println("boardDto = " + boardDto);
        return boardService.boardWrite(boardDto);
    }

    @GetMapping("/mylist")
    public boolean getMyList(){
        return boardService.listTest();
    }

    @PutMapping("/modify")
    public boolean boardModify(@RequestParam("boardNo") long boardNo,@RequestBody BoardDto boardDto){
        System.out.println("BoardController.boardModify");
        return boardService.boardModify(boardNo,boardDto);
    }

    @DeleteMapping("/delete")
    public boolean boardDelete(@RequestParam("boardNo") long boardNo){
        System.out.println("BoardController.boardDelete");
        return boardService.boardDelete(boardNo);
    }
}
