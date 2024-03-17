package com.application.service.board.service;

import com.application.common.domain.dto.boardService.BoardDto;
import com.application.common.domain.entity.boardService.BoardEntity;
import com.application.common.domain.entity.userService.UserEntity;
import com.application.common.util.jwtService.JwtUtils;
import com.application.service.board.repository.BoardEntityRepository;
import com.application.service.user.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BoardService {
    private final BoardEntityRepository boardEntityRepository;
    private final UserEntityRepository userEntityRepository;

    public boolean boardWrite(BoardDto boardDto){
        Optional<UserEntity> userEntity = userEntityRepository.findByUserEmail(
                ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()
        );
        System.out.println("로그인 정보 접근 가능 ?  "+ SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if(userEntity.isPresent()){

            BoardEntity board = boardDto.allToEntity();
            board.setUserEntity(userEntity.get());
            boardEntityRepository.save(board);
            userEntity.get().getBoardEntities().add(board);
            return true;
        }


        return false;
    }

    public boolean listTest(){
        userEntityRepository.findByUserEmail(
                ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()
        ).ifPresent(u->{
            for(BoardEntity b : u.getBoardEntities()){
                System.out.println(b.toString());
            }
        });
        return false;
    }

    public boolean boardModify(long boardNo,BoardDto boardDto){
        Optional<BoardEntity> boardEntityOptional = boardEntityRepository.findById(boardNo);
        System.out.println("boardNo = " + boardNo + ", boardDto = " + boardDto);
        if(boardEntityOptional.isPresent()){
            System.out.println("step1");
            if(boardEntityOptional.get().getUserEntity().getUserEmail().equals(JwtUtils.contextGetUserEmail())){
                System.out.println("step2");
                boardEntityOptional.get().setBoardContent(boardDto.getBoardContent());
                boardEntityOptional.get().setBoardTitle(boardDto.getBoardTitle());
                return true;
            }

        }

        return false;
    }

}
