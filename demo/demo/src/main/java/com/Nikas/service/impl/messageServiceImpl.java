package com.Nikas.service.impl;

import com.Nikas.entity.message;
import com.Nikas.entity.section;
import com.Nikas.entity.topic;
import com.Nikas.entity.user;
import com.Nikas.repo.MessageRepo;
import com.Nikas.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Nikas on 06.12.2016.
 */
@Service("MessageService")
@Transactional
public class messageServiceImpl implements MessageService
{
    @Autowired
    private MessageRepo mrp;

    @Override
    public message getByPmid(Integer id)
    {
        return mrp.getOne(id);
    };
    @Override
    public List<message> getByTid(topic sect)
    {
        return mrp.findByTpc(sect);
    };
    @Override
    public List<message> getByUid(user usr)
    {
        return mrp.findByUsr(usr);
    };
    @Override
    public List<message> getAll()
    {
        return mrp.findAll();
    };
    @Override
    public message addMessage(message msg)
    {
        return mrp.saveAndFlush(msg);
    };
    @Override
    public void deleteMessage(message msg)
    {
        mrp.delete(msg);
    };
    @Override
    public message editMessage(message tpc)
    {
        return mrp.saveAndFlush(tpc);
    };
}
