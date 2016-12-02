package com.Nikas.service.impl;

import com.Nikas.entity.user;

import com.Nikas.repo.UserRepo;
import com.Nikas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Created by Nikas on 01.12.2016.
 */

@Service("UserService")
@Transactional
public class userServiceImpl implements UserService {

    @Autowired
    private UserRepo usrRepo;

    @Override
    public user getByUid(UUID id)
    {
        return usrRepo.getOne(id);
    };
    @Override
    public user addUser(user usr)
    {
        user user = usrRepo.saveAndFlush(usr);
        return user;
    };
    @Override
    public void deleteUser(UUID id)
    {
        usrRepo.delete(id);
    };
    @Override
    public user getByName(String name)
    {
        return usrRepo.findByName(name);
    };
    @Override
    public user editUser(user usr)
    {
        user usre =usrRepo.saveAndFlush(usr);
        return usre;
    };
    @Override
    public List<user> getAll()
    {
        return (List<user>)usrRepo.findAll();
    };
}