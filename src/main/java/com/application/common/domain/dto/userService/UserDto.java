package com.application.common.domain.dto.userService;


import com.application.common.domain.entity.userService.UserEntity;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class UserDto implements UserDetails {
    private long userNo;
    @Pattern(regexp=("^[a-zA-Z가-힣]{1,10}$"), message="한글로 10글자 내로 입력 해주세요")
    private String userName;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W_])(?=.*[0-9]).{6,20}$",message = "대,소,특수문자 포함 6~20 사이 비밀번호 입력")
    private String userPassword;
    @Pattern(regexp = "^.{2,20}$", message = "2~20글자 사이 닉네임 입력")
    private String userNickname;
    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$",message = "000-0000-0000 형식에 맞춰서 입력")
    private String userPhone;
    @Email(message = "이메일 형식이 맞지 않습니다")
    private String userEmail;
    @Pattern(regexp = "^\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])-[1-4]$",message = "yymmdd-(0~4) 형식에 맞춰서 입력 해주세요")
    private String userBirth;
    @NotBlank
    @Pattern(regexp = "USER|ADMIN",message = "유효하지 않은 권한 입니다")
    private String userGradle;

    /* user Details */

    private boolean isCredentialsNonExpired; // 패스워드 만료 상태
    private boolean isAccountNonExpired;    // 계정 만료
    private boolean isAccountNonLocked;     //  계정 잠금
    private boolean isEnabled;              // 계정  활성 상태
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userGradle;
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
    public UserEntity toEntity() {
        return UserEntity.builder()
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
