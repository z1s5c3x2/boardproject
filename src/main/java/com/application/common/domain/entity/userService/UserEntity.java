package com.application.common.domain.entity.userService;

import com.application.common.domain.dto.userService.UserDto;
import com.application.common.domain.entity.boardService.BoardEntity;
import com.application.common.domain.entity.common.BaseTimeEntity;
import com.application.common.domain.entity.replyService.ReplyEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity @Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER")
public class UserEntity extends BaseTimeEntity {

    @Id
    @Column(name = "user_no")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userNo;

    @Column(nullable = false)
    private String userName;
    @Column(nullable = false)
    private String userPassword;
    @Column(nullable = false)
    private String userNickname;
    @Column(unique = true,nullable = false)
    private String userPhone;
    @Column(unique = true,nullable = false)
    private String userEmail;
    @Column(nullable = false)
    private String userBirth;
    @ColumnDefault("'USER'")
    private String userGrade;
    @Column(columnDefinition = "boolean default true",insertable = false)
    private boolean isCredentialsNonExpired;
    @Column(columnDefinition = "boolean default true",insertable = false)
    private boolean isAccountNonExpired;
    @Column(columnDefinition = "boolean default true",insertable = false)
    private boolean isAccountNonLocked;
    @Column(columnDefinition = "boolean default true",insertable = false)
    private boolean isEnabled;


    @OneToMany(mappedBy = "userEntity",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Builder.Default
    private List<BoardEntity> boardEntities = new ArrayList<>();

    @OneToMany(mappedBy = "userEntity",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Builder.Default
    private List<ReplyEntity> replyEntities = new ArrayList<>();

    public UserDto toDto() {
        return UserDto.builder()
                .userNo(this.userNo)
                .userName(this.userName)
                .userPassword(this.userPassword)
                .userNickname(this.userNickname)
                .userPhone(this.userPhone)
                .userEmail(this.userEmail)
                .userBirth(this.userBirth)
                .userGrade(this.userGrade)
                .isCredentialsNonExpired(this.isCredentialsNonExpired)
                .isAccountNonExpired(this.isAccountNonExpired)
                .isAccountNonLocked(this.isAccountNonLocked)
                .isEnabled(this.isEnabled)
                .build();
    }

    public UserDto loginToDto(){
        return UserDto.builder()
                .userEmail(this.userEmail)
                .userName(this.userName)
                .userPassword(this.userPassword)
                .userGrade(this.userGrade).build();
    }
}
