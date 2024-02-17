package com.application.service.user.repository;

import com.application.common.domain.entity.userService.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

}
