package com.Nikas.service.impl;

import com.Nikas.entity.section;
import com.Nikas.entity.topic;
import com.Nikas.entity.user;
import com.Nikas.repo.TopicRepo;
import com.Nikas.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Nikas on 05.12.2016.
 */
@Service("TopicService")
@Transactional
public class topicServiceImpl implements TopicService{

    @Autowired
    private TopicRepo topRepo;

    @Override
    public topic getByTid(Integer id)
    {return topRepo.getOne(id);};
    @Override
    public topic getByName(String name)
    {return topRepo.findByName(name);};
    @Override
    public List<topic> getBySectid(section sect)
    {
        return (List<topic>)topRepo.findBySectid(sect);
    };
    @Override
    public List<topic> getByCreator(user usr)
    {
        return (List<topic>)topRepo.findByCreator(usr);
    };
    @Override
    public List<topic> getAll()
    {
        return (List<topic>)topRepo.findAll();
    };
    @Override
    public topic addTopic(topic tpc)
    {
        return topRepo.saveAndFlush(tpc);
    };
    @Override
    public void deleteTopic(topic tpc)
    {
        topRepo.delete(tpc);
    };
    @Override
    public topic editTopic(topic tpc)
    {
        return topRepo.saveAndFlush(tpc);
    };
}

