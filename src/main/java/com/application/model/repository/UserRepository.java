package com.application.model.repository;

import com.application.model.dto.UserDto;
import com.application.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    void save(UserDto userDto);

}
