package com.Nikas.repo;

import com.Nikas.entity.section;
import com.Nikas.entity.topic;
import com.Nikas.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Nikas on 02.12.2016.
 */
public interface TopicRepo extends JpaRepository<topic, Integer> {
    topic findByName(String name);
    List<topic> findByCreator(user usr);
    List<topic> findBySectid(section sect);
}
