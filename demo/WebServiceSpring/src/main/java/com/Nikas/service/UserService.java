package com.Nikas.service;

import com.Nikas.entity.user;

import java.util.List;
import java.util.UUID;

/**
 * Created by Nikas on 01.12.2016.
 */
public interface UserService {
    user addUser(user usr);
    void deleteUser(UUID id);
    user getByName(String name);
    user editUser(user usr);
    List<user> getAll();
}
