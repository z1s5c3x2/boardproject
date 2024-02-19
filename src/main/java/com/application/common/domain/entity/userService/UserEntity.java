package com.application.common.domain.entity.userService;

import com.application.common.domain.dto.userService.UserDto;
import com.application.common.domain.entity.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity @Getter @Setter
@Builder @ToString
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
    private String userGradle;
    @ColumnDefault("true")
    private boolean isCredentialsNonExpired;
    @ColumnDefault("true")
    private boolean isAccountNonExpired;
    @ColumnDefault("true")
    private boolean isAccountNonLocked;
    @ColumnDefault("true")
    private boolean isEnabled;


    public UserDto toDto() {
        return UserDto.builder()
                .userNo(this.userNo)
                .userName(this.userName)
                .userPassword(this.userPassword)
                .userNickname(this.userNickname)
                .userPhone(this.userPhone)
                .userEmail(this.userEmail)
                .userBirth(this.userBirth)
                .userGradle(this.userGradle)
                .isCredentialsNonExpired(this.isCredentialsNonExpired)
                .isAccountNonExpired(this.isAccountNonExpired)
                .isAccountNonLocked(this.isAccountNonLocked)
                .isEnabled(this.isEnabled)
                .build();
    }
}
