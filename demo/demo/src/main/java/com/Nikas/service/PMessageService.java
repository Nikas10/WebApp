package com.Nikas.service;

import com.Nikas.entity.privatemessage;
import com.Nikas.entity.user;

import java.util.List;

/**
 * Created by Nikas on 08.12.2016.
 */
public interface PMessageService {
    privatemessage getByPrmid(Integer id);
    List<privatemessage> getBySender(user usr);
    List<privatemessage> getByReceiver(user usr);
    List<privatemessage> getAll();
    privatemessage addPMessage(privatemessage msg);
    void deletePMessage(privatemessage msg);
}
