package com.Nikas.repo;

import com.Nikas.entity.privatemessage;
import com.Nikas.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Nikas on 08.12.2016.
 */
public interface PMessageRepo extends JpaRepository<privatemessage, Integer> {
    List<privatemessage> findBySender(user usr);
    List<privatemessage> findByReceiver(user usr);
    privatemessage findByPrmid(Integer id);
}
