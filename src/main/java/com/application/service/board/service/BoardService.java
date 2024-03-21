package com.application.service.board.service;

import com.application.common.domain.dto.boardService.BoardDto;
import com.application.common.domain.dto.common.PageDto;
import com.application.common.domain.entity.boardService.BoardEntity;
import com.application.common.domain.entity.userService.UserEntity;
import com.application.common.util.jwtService.JwtUtils;
import com.application.service.board.repository.BoardEntityRepository;
import com.application.service.user.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BoardService {
    private final BoardEntityRepository boardEntityRepository;
    private final UserEntityRepository userEntityRepository;

    public PageDto<BoardDto> getBoardList(int page,int size){
        Pageable pageable = PageRequest.of(page-1,size);
        Page<BoardEntity> boardEntityPage = boardEntityRepository.findAll(pageable);

        return PageDto.<BoardDto>builder()
                .someList(boardEntityPage.stream().map(BoardEntity::allToDto).collect(Collectors.toList()))
                .totalCount(boardEntityPage.getTotalElements())
                .totalPages(boardEntityPage.getTotalPages()).build();
    }
    public boolean boardWrite(BoardDto boardDto){
        Optional<UserEntity> userEntity = userEntityRepository.findByUserEmail(
                JwtUtils.contextGetUserEmail()
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
                JwtUtils.contextGetUserEmail()
        ).ifPresent(u->{
            for(BoardEntity b : u.getBoardEntities()){
                System.out.println(b.toString());
            }
        });
        return false;
    }

    public boolean boardModify(long boardNo,BoardDto boardDto){
        Optional<BoardEntity> boardEntityOptional = boardEntityRepository.findById(boardNo);
        if(boardEntityOptional.isPresent()){
            if(checkOwner(boardEntityOptional.get())){
                boardEntityOptional.get().setBoardContent(boardDto.getBoardContent());
                boardEntityOptional.get().setBoardTitle(boardDto.getBoardTitle());
                return true;
            }
        }
        return false;
    }

    public boolean boardDelete(long boardNo){
        Optional<BoardEntity> boardEntityOptional = boardEntityRepository.findById(boardNo);
        if(boardEntityOptional.isPresent()){
            if(checkOwner(boardEntityOptional.get())){
                boardEntityRepository.delete(boardEntityOptional.get());
                return true;
            }
        }
        return false;
    }

    private boolean checkOwner(BoardEntity boardEntity){
        return JwtUtils.contextGetUserEmail().equals(boardEntity.getUserEntity().getUserEmail());
    }
}
