package com.application.service.board.controller;

import com.application.common.domain.dto.boardService.BoardDto;
import com.application.service.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@Slf4j
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/list")
    public String asd(){

        return "게시글 정보";
    }

    @PostMapping("/post")
    public boolean boardWrite(@RequestBody BoardDto boardDto){
        System.out.println("boardDto = " + boardDto);
        boardService.boardWrite(boardDto);
        return true;
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
}
