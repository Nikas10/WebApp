package com.Nikas.Servlet;

import com.Nikas.entity.section;
import com.Nikas.entity.topic;
import com.Nikas.entity.user;
import com.Nikas.pojo.enums.KnownExceptions;
import com.Nikas.pojo.logForm;
import com.Nikas.pojo.respForm;
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
import java.util.UUID;

/**
 * Created by Nikas on 05.12.2016.
 */

@RestController
public class TopicController {

    @Resource(name = "SectionService")
    sectionServiceImpl sectr;
    @Resource(name="UserService")
    userServiceImpl usi;
    @Resource(name="TopicService")
    topicServiceImpl tps;

    ObjectMapper obm = new ObjectMapper();

    public logForm lf = new logForm();
    public respForm rf = new respForm();

    @RequestMapping(value="/topic/add", method= RequestMethod.POST)
    public  String add(HttpServletRequest form) throws JsonProcessingException
    {
        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("topicname")==null)
                || (form.getParameter("topicname").equals(""))
                || (form.getParameter("sectid")== null)
                || (form.getParameter("sectid").equals(""))
                || (form.getParameter("desc")== null)
                || (form.getParameter("desc").equals(""))
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
        topic check = tps.getByName(form.getParameter("topicname"));
        if (check!=null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.TopicExistsException);
            rf.setMessage("Topic with that name already exists.");
            return obm.writeValueAsString(rf);
        }
        section chec = sectr.getBySid(Integer.parseInt(form.getParameter("sectid")));
        if (chec==null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoSectionException);
            rf.setMessage("Section with that id does not exists.");
            return obm.writeValueAsString(rf);
        }
        topic top = new topic();
        top.setName(form.getParameter("topicname"));
        top.setCreator(usr);
        top.setSectid(chec);
        top.setDescription(form.getParameter("desc"));
        if ((form.getParameter("status")==null)||(form.getParameter("status").equals("")))
        {
            Short a = 1;
            top.setStatus(a);
        }else
        {
            if (usr.getLevel()<Integer.parseInt(form.getParameter("status")))
            {
                rf.setError();
                rf.setErrortype(KnownExceptions.AccessDenyException);
                rf.setMessage("Your level is lower than needed to make a private topic of this level.");
                return obm.writeValueAsString(rf);
            }else
                top.setStatus(Short.parseShort(form.getParameter("status")));
        }
        Calendar t = Calendar.getInstance();
        top.setDate(new Timestamp(t.getTimeInMillis()));
        tps.addTopic(top);

        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Topic successfully added.");
        return obm.writeValueAsString(rf);
    }





    @RequestMapping(value="/topic/delete",method= RequestMethod.POST)
    public String Deletetopic(HttpServletRequest form) throws JsonProcessingException
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
        if (usr.getLevel()<2)
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
        if (usr.getLevel()<check.getStatus())
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.AccessDenyException);
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        tps.deleteTopic(check);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Topic successfully deleted.");
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value="/topic/edit",method= RequestMethod.POST)
    public String Edittopic(HttpServletRequest form) throws JsonProcessingException
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
        if (usr.getLevel()<2)
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
        if ((form.getParameter("sid")!=null)||(!form.getParameter("sid").equals("")))
        {
            section sect = sectr.getBySid(Integer.parseInt(form.getParameter("sid")));
            if (sect==null)
            {
                rf.setError();
                rf.setErrortype(KnownExceptions.NoSectionException);
                rf.setMessage("Section with that name does not exists.");
                return obm.writeValueAsString(rf);
            }
            else check.setSectid(sect);
        }
        if ((form.getParameter("newname")!=null)||(!form.getParameter("newname").equals(""))) {
            topic ch = tps.getByName(form.getParameter("newname"));
            if (ch!=null)
            {
                rf.setError();
                rf.setErrortype(KnownExceptions.TopicExistsException);
                rf.setMessage("Topic with that name already exists.");
                return obm.writeValueAsString(rf);
            }
            else check.setName(form.getParameter("newname"));
        }
        if ((form.getParameter("desc")!=null)||(!form.getParameter("desc").equals("")))
            check.setDescription(form.getParameter("desc"));
        if ((form.getParameter("status")!=null)||(!form.getParameter("status").equals("")))
        {
            if (usr.getLevel()<Integer.parseInt(form.getParameter("status")))
            {
                rf.setError();
                rf.setErrortype(KnownExceptions.AccessDenyException);
                rf.setMessage("You don't have enough rights to do this operation.");
                return obm.writeValueAsString(rf);
            }
        }
        tps.editTopic(check);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Topic successfully edited.");
        return obm.writeValueAsString(rf);
    }


    @RequestMapping(value="/topic/get/name",method= RequestMethod.POST)
    public String getTopicByName(HttpServletRequest form) throws JsonProcessingException
    {
        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("topicname")==null)
                || (form.getParameter("topicname").equals(""))
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
        topic check = tps.getByName(form.getParameter("topicname"));
        if (check==null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoTopicException);
            rf.setMessage("Topic with that name does not exists.");
            return obm.writeValueAsString(rf);
        }
        List<topic> lst = new ArrayList<topic>();
        lst.add(check);
        rf.setContent(lst);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Topic successfully deleted.");
        return obm.writeValueAsString(rf);
    }
    @RequestMapping(value="/topic/get/id",method= RequestMethod.POST)
    public String getTopicById(HttpServletRequest form) throws JsonProcessingException
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
        List<topic> lst = new ArrayList<topic>();
        lst.add(check);
        rf.setContent(lst);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Topic successfully sended.");
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value="/topic/get/section",method= RequestMethod.POST)
    public String getTopicBySection(HttpServletRequest form) throws JsonProcessingException
    {
        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("sectid")==null)
                || (form.getParameter("sectid").equals(""))
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
        section check = sectr.getBySid(Integer.parseInt(form.getParameter("sectid")));
        if (check==null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoSectionException);
            rf.setMessage("Section with that name does not exists.");
            return obm.writeValueAsString(rf);
        }
        List<topic> lst = tps.getBySectid(check);
        if (lst==null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoTopicException);
            rf.setMessage("Topic with that name does not exists.");
            return obm.writeValueAsString(rf);
        }
        rf.setContent(lst);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Topics successfully sended.");
        return obm.writeValueAsString(rf);
    }
    @RequestMapping(value="/topic/get/user",method= RequestMethod.POST)
    public String getTopicByUser(HttpServletRequest form) throws JsonProcessingException
    {
        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("uid")==null)
                || (form.getParameter("uid").equals(""))
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
        user check = usi.getByUid(UUID.fromString(form.getParameter("uid")));
        if (check==null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoUserException);
            rf.setMessage("User with that name does not exists.");
            return obm.writeValueAsString(rf);
        }
        List<topic> lst = tps.getByCreator(check);
        if (lst==null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoTopicException);
            rf.setMessage("Topic with that name does not exists.");
            return obm.writeValueAsString(rf);
        }
        rf.setContent(lst);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Topics successfully sended.");
        return obm.writeValueAsString(rf);
    }
}
