package com.application.common.domain.dto.jwtService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TokenDto {
    private String accessToken;
    private String refreshToken;

    public boolean isNull(){
        return accessToken == null && refreshToken == null;
    }
}
