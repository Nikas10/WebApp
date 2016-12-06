package com.Nikas.repo;

/**
 * Created by Nikas on 03.12.2016.
 */
import com.Nikas.entity.message;
import com.Nikas.entity.topic;
import com.Nikas.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepo extends JpaRepository<message, Integer>{
    List<message> findByTpc(topic tpc);
    List<message> findByUsr(user usr);
    message findByPmid(Integer id);
}
