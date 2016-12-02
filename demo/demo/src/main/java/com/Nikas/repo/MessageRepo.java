package com.Nikas.repo;

/**
 * Created by Nikas on 03.12.2016.
 */
import com.Nikas.entity.message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<message, Integer>{
}
