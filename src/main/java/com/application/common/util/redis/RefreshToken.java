package com.application.common.util.redis;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "refresh",timeToLive = 3000)
@ToString
public class RefreshToken {
    @Id @Indexed
    private String userEmail;

    private String refreshToken;

}
