package com.Nikas.service;

import com.Nikas.entity.section;

import java.util.List;


/**
 * Created by Nikas on 04.12.2016.
 */
public interface SectionService {
   section getBySid(Integer id);
   section getByName(String name);
   section addSection(section sect);
   void deleteSection(Integer section);
   section editSection(section sect);
   List<section> getAll();
}
