package com.Nikas.service;

import com.Nikas.entity.ban;
import com.Nikas.entity.user;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Nikas on 08.12.2016.
 */
public interface BanService {
    List<ban> getByModerator(user usr);
    List<ban> getByUserban(user usr);
    List<ban> getByUntil(Timestamp until);
    ban getByBid(Integer id);
    ban addBan(ban bn);
    void delBan(ban bn);
}
