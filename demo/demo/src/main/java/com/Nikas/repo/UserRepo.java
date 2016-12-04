package com.Nikas.repo;

import java.util.UUID;

import com.Nikas.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<user, UUID> {
    user findByName(String name);
}