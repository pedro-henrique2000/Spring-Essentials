package com.projects.essentials.repository;

import com.projects.essentials.domain.DevdojoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevdojoUserRepository extends JpaRepository<DevdojoUser, Long> {
    DevdojoUser findByUsername(String name);
}
