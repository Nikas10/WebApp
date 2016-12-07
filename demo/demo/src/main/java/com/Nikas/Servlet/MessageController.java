package com.Nikas.Servlet;

import com.Nikas.entity.message;
import com.Nikas.entity.topic;
import com.Nikas.entity.user;
import com.Nikas.pojo.enums.KnownExceptions;
import com.Nikas.pojo.logForm;
import com.Nikas.pojo.respForm;
import com.Nikas.service.impl.messageServiceImpl;
import com.Nikas.service.impl.sectionServiceImpl;
import com.Nikas.service.impl.topicServiceImpl;
import com.Nikas.service.impl.userServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    @RequestMapping(value="/message/add", method= RequestMethod.POST)
    public  String add(HttpServletRequest form) throws JsonProcessingException
    {
        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("topicid")==null)
                || (form.getParameter("topicid").equals(""))
                || (form.getParameter("text")== null)
                || (form.getParameter("text").equals(""))
                )
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NotEnoughDataException);
            rf.setMessage("Some required fields are empty");
            return obm.writeValueAsString(rf);
        }
        lf.setName(form.getParameter("name"));
        lf.setPass(form.getParameter("pass"));
        rf= usi.checkLogindata(lf.getName(),lf.getPass());
        if (rf.getStatus().equals("error"))
        {
            return obm.writeValueAsString(rf);
        }
        user usr = usi.getByName(lf.getName());
        if (usr.getLevel()<1)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.AccessDenyException);
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        topic check = tps.getByTid(Integer.parseInt(form.getParameter("topicid")));
        if ( check == null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoTopicException);
            rf.setMessage("Topic with that name does not exists.");
            return obm.writeValueAsString(rf);
        }
        if (usr.getLevel()<check.getStatus())
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.AccessDenyException);
            rf.setMessage("Your level is lower than needed to post a message in this topic.");
            return obm.writeValueAsString(rf);
        }
        message msg = new message();
        msg.setTpc(check);
        msg.setUsr(usr);
        msg.setText(form.getParameter("text"));
        if ((form.getParameter("imglink")== null)
                || (form.getParameter("imglink").equals("")));
        {
            msg.setImglink(form.getParameter("imglink"));
        }
        Calendar t = Calendar.getInstance();
        msg.setDate(new Timestamp(t.getTimeInMillis()));
        mrp.addMessage(msg);

        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Section successfully added.");
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value="/message/delete", method= RequestMethod.POST)
    public  String del(HttpServletRequest form) throws JsonProcessingException
    {
        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("message")==null) //id
                || (form.getParameter("message").equals(""))
                )
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NotEnoughDataException);
            rf.setMessage("Some required fields are empty");
            return obm.writeValueAsString(rf);
        }
        lf.setName(form.getParameter("name"));
        lf.setPass(form.getParameter("pass"));
        rf= usi.checkLogindata(lf.getName(),lf.getPass());
        if (rf.getStatus().equals("error"))
        {
            return obm.writeValueAsString(rf);
        }
        user usr = usi.getByName(lf.getName());
        if (usr.getLevel()<1)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.AccessDenyException);
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        message msg = mrp.getByPmid(Integer.parseInt(form.getParameter("message")));
        if (msg == null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoMessageException);
            rf.setMessage("Message with that id does not exists.");
            return obm.writeValueAsString(rf);
        }
        topic check = msg.getTpc();
        if (usr.getLevel()<check.getStatus())
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.AccessDenyException);
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        if ((usr.getUid()==msg.getUsr().getUid())||(usr.getLevel()>msg.getUsr().getLevel()))
        {
            mrp.deleteMessage(msg);
            rf.setSuccess();
            rf.setErrortype("none");
            rf.setMessage("Section successfully added.");
            return obm.writeValueAsString(rf);
        }
        else
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.AccessDenyException);
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
    }


    @RequestMapping(value="/message/edit", method= RequestMethod.POST)
    public  String edit(HttpServletRequest form) throws JsonProcessingException
    {
        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("message")==null) //id
                || (form.getParameter("message").equals(""))
                )
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NotEnoughDataException);
            rf.setMessage("Some required fields are empty");
            return obm.writeValueAsString(rf);
        }
        lf.setName(form.getParameter("name"));
        lf.setPass(form.getParameter("pass"));
        rf= usi.checkLogindata(lf.getName(),lf.getPass());
        if (rf.getStatus().equals("error"))
        {
            return obm.writeValueAsString(rf);
        }
        user usr = usi.getByName(lf.getName());
        if (usr.getLevel()<1)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.AccessDenyException);
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }

        message msg = mrp.getByPmid(Integer.parseInt(form.getParameter("message")));
        if (msg==null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoMessageException);
            rf.setMessage("Message with that id does not exists.");
            return obm.writeValueAsString(rf);
        }
        topic check = msg.getTpc();
        if (usr.getLevel()<check.getStatus())
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.AccessDenyException);
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        if ((usr.getUid()==msg.getUsr().getUid())||(usr.getLevel()>msg.getUsr().getLevel())) {
            if ((form.getParameter("text") != null) //id
                    || (!form.getParameter("text").equals(""))) {
                msg.setText(form.getParameter("text"));
            }
            if ((form.getParameter("imglink") != null) //id
                    || (!form.getParameter("imglink").equals(""))) {
                msg.setImglink(form.getParameter("imglink"));
            }
            if ((form.getParameter("topicid") != null) //id
                    || (!form.getParameter("topicid").equals(""))) {
                check = tps.getByTid(Integer.parseInt(form.getParameter("topicid")));
                if (check == null) {
                    rf.setError();
                    rf.setErrortype(KnownExceptions.NoTopicException);
                    rf.setMessage("Topic with that name does not exists.");
                    return obm.writeValueAsString(rf);
                } else if (check.getStatus() > usr.getLevel()) {
                    rf.setError();
                    rf.setErrortype(KnownExceptions.AccessDenyException);
                    rf.setMessage("You don't have enough rights to do this operation.");
                    return obm.writeValueAsString(rf);
                } else msg.setTpc(check);
            }
        }

        mrp.editMessage(msg);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Section successfully added.");
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value="/message/get/id", method= RequestMethod.POST)
    public  String getbyid(HttpServletRequest form) throws JsonProcessingException
    {
        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("message")==null)
                || (form.getParameter("message").equals(""))
                )
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NotEnoughDataException);
            rf.setMessage("Some required fields are empty");
            return obm.writeValueAsString(rf);
        }
        lf.setName(form.getParameter("name"));
        lf.setPass(form.getParameter("pass"));
        rf= usi.checkLogindata(lf.getName(),lf.getPass());
        if (rf.getStatus().equals("error"))
        {
            return obm.writeValueAsString(rf);
        }
        user usr = usi.getByName(lf.getName());
        if (usr.getLevel()<1)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.AccessDenyException);
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        message msg = mrp.getByPmid(Integer.parseInt(form.getParameter("message")));
        if (msg==null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoMessageException);
            rf.setMessage("Message with that id does not exists.");
            return obm.writeValueAsString(rf);
        }
        topic check = msg.getTpc();
        if (check.getStatus()>usr.getLevel())
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.AccessDenyException);
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        List<message> lst=  new ArrayList<message>();
        lst.add(msg);
        rf.setContent(lst);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Section successfully added.");
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value="/message/get/topic", method= RequestMethod.POST)
    public  String getbytopic(HttpServletRequest form) throws JsonProcessingException
    {
        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("topicid")==null)
                || (form.getParameter("topicid").equals(""))
                )
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NotEnoughDataException);
            rf.setMessage("Some required fields are empty");
            return obm.writeValueAsString(rf);
        }
        lf.setName(form.getParameter("name"));
        lf.setPass(form.getParameter("pass"));
        rf= usi.checkLogindata(lf.getName(),lf.getPass());
        if (rf.getStatus().equals("error"))
        {
            return obm.writeValueAsString(rf);
        }
        user usr = usi.getByName(lf.getName());
        if (usr.getLevel()<1)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.AccessDenyException);
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }

        topic check = tps.getByTid(Integer.parseInt(form.getParameter("topicid")));
        if (check==null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoTopicException);
            rf.setMessage("Topic with that name does not exists.");
            return obm.writeValueAsString(rf);
        }
        if (check.getStatus()>usr.getLevel())
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.AccessDenyException);
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        List<message> lst = mrp.getByTid(check);
        rf.setContent(lst);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Section successfully added.");
        return obm.writeValueAsString(rf);
    }
    @RequestMapping(value="/message/get/user", method= RequestMethod.POST)

    public  String getbyuser(HttpServletRequest form) throws JsonProcessingException
    {
        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("username")==null)
                || (form.getParameter("username").equals(""))
                )
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NotEnoughDataException);
            rf.setMessage("Some required fields are empty");
            return obm.writeValueAsString(rf);
        }
        lf.setName(form.getParameter("name"));
        lf.setPass(form.getParameter("pass"));
        rf= usi.checkLogindata(lf.getName(),lf.getPass());
        if (rf.getStatus().equals("error"))
        {
            return obm.writeValueAsString(rf);
        }
        user usr = usi.getByName(lf.getName());
        if (usr.getLevel()<2)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.AccessDenyException);
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        user check = usi.getByName(form.getParameter("username"));
        if (check==null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoUserException);
            rf.setMessage("User with that username does not exists.");
            return obm.writeValueAsString(rf);
        }
        if (check.getLevel()>usr.getLevel())
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.AccessDenyException);
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        List<message> lst = check.getPublicmsgs();
        rf.setContent(lst);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Section successfully added.");
        return obm.writeValueAsString(rf);
    }


}

