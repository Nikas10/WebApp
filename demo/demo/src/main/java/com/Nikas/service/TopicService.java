package com.Nikas.service;

import com.Nikas.entity.section;
import com.Nikas.entity.topic;
import com.Nikas.entity.user;

import java.util.List;

/**
 * Created by Nikas on 04.12.2016.
 */
public interface TopicService {
    topic getByTid(Integer id);
    topic getByName(String name);
    List<topic> getBySectid(section sect);
    List<topic> getByCreator(user usr);
    List<topic> getAll();
    topic addTopic(topic tpc);
    void deleteTopic(topic tpc);
    topic editTopic(topic tpc);
}
