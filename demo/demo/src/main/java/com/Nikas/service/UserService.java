package com.Nikas.service;

import com.Nikas.entity.section;
import com.Nikas.entity.user;
import com.Nikas.pojo.respForm;

import java.util.List;
import java.util.UUID;

/**
 * Created by Nikas on 01.12.2016.
 */
public interface UserService {

    user getByMail(String mail);
    user getByUid(UUID id);
    user addUser(user usr);
    void deleteUser(UUID id);
    user getByName(String name);
    user editUser(user usr);
    List<user> getAll();
    respForm checkLogindata(String name, String pass);
    user addSection(user usr, section sect);
}
