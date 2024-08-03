package com.lenin.securedoc.repository;

import com.lenin.securedoc.entity.ConfirmationEntity;
import com.lenin.securedoc.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationRepository extends JpaRepository<ConfirmationEntity, Long> {
    Optional<ConfirmationEntity> findByConfirmKey(String key);
    Optional<ConfirmationEntity> findByUserEntity (UserEntity userEntity);
}
