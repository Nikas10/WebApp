package com.Nikas.repo;

import com.Nikas.entity.ban;
import com.Nikas.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Nikas on 08.12.2016.
 */
public interface BanRepo extends JpaRepository<ban,Integer> {
    List<ban> findByModerator(user usr);
    List<ban> findByUserban(user usr);
    ban findByBid(Integer id);
    List<ban> findByUntil(Timestamp until);
}
