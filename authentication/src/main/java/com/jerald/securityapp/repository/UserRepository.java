package com.jerald.securityapp.repository;

import com.jerald.securityapp.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    Users findByEmail( String email);

    boolean existsByEmail(String email);
}
