package com.Nikas.repo;

import com.Nikas.entity.section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Nikas on 02.12.2016.
 */
public interface SectionRepo extends JpaRepository<section, Integer> {

    section findByName(String name);
    List<section> findByParsect(section parent);
}
