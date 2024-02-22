package com.application.service.redis.cdi;

import com.application.common.util.redis.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken,String>  {
    Optional<RefreshToken> findByUserEmail(String userEmail);
}
