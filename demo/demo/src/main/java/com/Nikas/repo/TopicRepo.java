package com.Nikas.repo;

import com.Nikas.entity.topic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Nikas on 02.12.2016.
 */
public interface TopicRepo extends JpaRepository<topic, Integer> {
}
