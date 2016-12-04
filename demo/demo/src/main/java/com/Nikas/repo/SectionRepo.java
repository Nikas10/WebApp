package com.Nikas.repo;

import com.Nikas.entity.section;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Nikas on 02.12.2016.
 */
public interface SectionRepo extends JpaRepository<section, Integer> {
    section findByName(String name);
}