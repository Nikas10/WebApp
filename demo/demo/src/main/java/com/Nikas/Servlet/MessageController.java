package com.Nikas.Servlet;

import com.Nikas.pojo.logForm;
import com.Nikas.pojo.respForm;
import com.Nikas.service.impl.messageServiceImpl;
import com.Nikas.service.impl.sectionServiceImpl;
import com.Nikas.service.impl.topicServiceImpl;
import com.Nikas.service.impl.userServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by Nikas on 06.12.2016.
 */
@RestController
public class MessageController {
    @Resource(name = "MessageService")
    messageServiceImpl mrp;
    @Resource(name="UserService")
    userServiceImpl usi;
    @Resource(name="TopicService")
    topicServiceImpl tps;

    ObjectMapper obm = new ObjectMapper();

    public logForm lf = new logForm();
    public respForm rf = new respForm();

}
