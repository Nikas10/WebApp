package com.Nikas.service;

import com.Nikas.entity.message;
import com.Nikas.entity.topic;
import com.Nikas.entity.user;

import java.util.List;

/**
 * Created by Nikas on 04.12.2016.
 */
public interface MessageService {
    message getByPmid(Integer id);
    List<message> getByTid(topic sect);
    List<message> getByUid(user usr);
    List<message> getAll();
    message addMessage(message msg);
    void deleteMessage(message msg);
    message editMessage(message tpc);
}
