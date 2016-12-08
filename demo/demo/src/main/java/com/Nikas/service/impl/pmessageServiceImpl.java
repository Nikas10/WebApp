package com.Nikas.service.impl;

import com.Nikas.entity.privatemessage;
import com.Nikas.entity.user;
import com.Nikas.repo.PMessageRepo;
import com.Nikas.service.PMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Nikas on 08.12.2016.
 */
@Service("PMessageService")
@Transactional
public class pmessageServiceImpl implements PMessageService {
    @Autowired
    PMessageRepo pmr;

    @Override
    public privatemessage getByPrmid(Integer id) {
        return pmr.findByPrmid(id);
    }

    @Override
    public List<privatemessage> getBySender(user usr) {
        return pmr.findBySender(usr);
    }

    @Override
    public List<privatemessage> getByReceiver(user usr) {
        return pmr.findByReceiver(usr);
    }

    @Override
    public List<privatemessage> getAll() {
        return pmr.findAll();
    }

    @Override
    public privatemessage addPMessage(privatemessage msg) {
        return pmr.saveAndFlush(msg);
    }

    @Override
    public void deletePMessage(privatemessage msg) {
        pmr.delete(msg);
    }
}
