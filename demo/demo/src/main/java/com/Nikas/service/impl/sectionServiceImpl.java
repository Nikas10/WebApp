package com.Nikas.service.impl;

import com.Nikas.entity.section;
import com.Nikas.repo.SectionRepo;
import com.Nikas.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Nikas on 04.12.2016.
 */
@Service("SectionService")
@Transactional

public class sectionServiceImpl implements SectionService {
    @Autowired
    private SectionRepo sectRepo;

    @Override
    public section getBySid(Integer id)
    {return sectRepo.getOne(id);};
    @Override
    public section getByName(String name)
    {return  sectRepo.findByName(name);};
    @Override
    public section addSection(section sect)
    {return sectRepo.saveAndFlush(sect);};
    @Override
    public void deleteSection(Integer section)
    {sectRepo.delete(section);};
    @Override
    public section editSection(section sect)
    {return sectRepo.saveAndFlush(sect);};
    @Override
    public  List<section>getAll()
    {return (List<section>)sectRepo.findAll();};
}
