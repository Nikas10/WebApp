package com.Nikas.service.impl;

import com.Nikas.entity.ban;
import com.Nikas.entity.section;
import com.Nikas.entity.user;

import com.Nikas.pojo.enums.KnownExceptions;
import com.Nikas.pojo.respForm;
import com.Nikas.repo.BanRepo;
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
    @Autowired
    private BanRepo brp;

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
    public user getByMail(String mail)
    {
        return usrRepo.findByMail(mail);
    }
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

    @Override
    public respForm checkLogindata(String name, String pass) //ready
    {
        respForm rf = new respForm();
        user dbo = usrRepo.findByName(name);
        if (dbo!=null)
        {
            if (!pass.equals(dbo.getPassword()))
            {
                rf.setStatus("error");
                rf.setErrortype(KnownExceptions.LoginException);
                rf.setMessage("Your password or username is invalid.");
                return rf;
            }
            else
            {
                List<ban> any = dbo.getInbans();
                if (any!=null)
                {
                    rf.setStatus("error");
                    rf.setErrortype(KnownExceptions.UserBannedException);
                    rf.setMessage("User with that name is banned. Wait until tha ban is expired.");
                    return rf;
                }
                rf.setStatus("success");
                rf.setErrortype(KnownExceptions.none);
                rf.setMessage("Login successful.");
                return rf;
            }
        }
        {
            rf.setStatus("error");
            rf.setErrortype(KnownExceptions.NoUserException);
            rf.setMessage("User not found. Check your username or register.");
            return rf;
        }
    };

    @Override
    public user addSection(user usr,section sect)
    {
        List<section> lst = usr.getSections();
        lst.add(sect);
        usr.setSections(lst);
        return usrRepo.saveAndFlush(usr);
    };
}