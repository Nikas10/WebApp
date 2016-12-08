package com.Nikas.service.impl;

import com.Nikas.entity.ban;
import com.Nikas.entity.user;
import com.Nikas.repo.BanRepo;
import com.Nikas.service.BanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Nikas on 08.12.2016.
 */
@Service("BanService")
@Transactional
public class banServiceImpl implements BanService{
    @Autowired
    private BanRepo brp;


    @Override
    public List<ban> getByModerator(user usr) {
        return brp.findByModerator(usr);
    }

    @Override
    public List<ban> getByUserban(user usr) {
        return brp.findByUserban(usr);
    }

    @Override
    public List<ban> getByUntil(Timestamp until) {
        return brp.findByUntil(until);
    }

    @Override
    public ban getByBid(Integer id) {
        return brp.findOne(id);
    }

    @Override
    public ban addBan(ban bn) {
        return brp.saveAndFlush(bn);
    }

    @Override
    public void delBan(ban bn) {
        brp.delete(bn);
    }
}
