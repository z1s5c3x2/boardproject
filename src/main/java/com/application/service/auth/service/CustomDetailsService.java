package com.application.service.auth.service;

import com.application.common.domain.entity.userService.UserEntity;
import com.application.service.user.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CustomDetailsService implements UserDetailsService {
    private final UserEntityRepository userEntityRepository;
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        log.info(" login userEmail = {}", userEmail);
        Optional<UserEntity> optionalUserEntity = userEntityRepository.findByUserEmail(userEmail);

        if (!optionalUserEntity.isPresent()) {
            throw new UsernameNotFoundException("user not found");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(optionalUserEntity.get().getUserGrade()));
        return optionalUserEntity.get().toDto();

    }
}
