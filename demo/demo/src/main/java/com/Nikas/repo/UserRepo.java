package com.Nikas.repo;

import java.util.UUID;

import com.Nikas.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends JpaRepository<user, UUID> {

    @Query("select b from user b where b.name = :parameter")
    user findByName(@Param("parameter") String name);

}