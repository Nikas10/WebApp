package com.Nikas.Servlet;

import com.Nikas.entity.privatemessage;
import com.Nikas.entity.user;
import com.Nikas.pojo.enums.KnownExceptions;
import com.Nikas.pojo.logForm;
import com.Nikas.pojo.respForm;
import com.Nikas.service.impl.pmessageServiceImpl;
import com.Nikas.service.impl.userServiceImpl;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.StdArraySerializers;
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
 * Created by Nikas on 08.12.2016.
 */
@RestController
public class PMessageController {

    @Resource(name = "PMessageService")
    pmessageServiceImpl mrp;
    @Resource(name="UserService")
    userServiceImpl usi;

    ObjectMapper obm = new ObjectMapper();


    public logForm lf = new logForm();
    public respForm rf = new respForm();

    @RequestMapping(value="/pmessage/send", method= RequestMethod.POST)
    public  String add(HttpServletRequest form) throws JsonProcessingException
    {

        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("receiver")==null)
                || (form.getParameter("receiver").equals(""))
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
        user check = usi.getByName(form.getParameter("receiver"));
        if (check==null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoUserException);
            rf.setMessage("User with that name does not exists");
            return obm.writeValueAsString(rf);
        }
        List<user> blacklst = check.getBanned();
        if (blacklst.contains(usr))
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.UsrBlackListedException);
            rf.setMessage("You are blacklisted by this user.");
            return obm.writeValueAsString(rf);
        }
        privatemessage msg = new privatemessage();
        msg.setSender(usr);
        msg.setReceiver(check);
        msg.setText(form.getParameter("text"));
        if ((form.getParameter("imglink")== null)
                || (form.getParameter("imglink").equals("")));
        {
            msg.setImglink(form.getParameter("imglink"));
        }
        Calendar t = Calendar.getInstance();
        msg.setDate(new Timestamp(t.getTimeInMillis()));
        msg.setViewed(Boolean.FALSE);
        mrp.addPMessage(msg);

        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Private message successfully sended.");
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value="/pmessage/delete", method= RequestMethod.POST)
    public  String delete(HttpServletRequest form) throws JsonProcessingException
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
        privatemessage msg = mrp.getByPrmid(Integer.parseInt(form.getParameter("message")));
        if (msg == null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoPMessageException);
            rf.setMessage("Message with that id does not exists.");
            return obm.writeValueAsString(rf);
        }
        user check = msg.getSender();
        if (!usr.equals(check))
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.AccessDenyException);
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        mrp.deletePMessage(msg);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Private message successfully deleted.");
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value="/pmessage/broadcast", method= RequestMethod.POST)
    public  String broadcast(HttpServletRequest form) throws JsonProcessingException
    {
        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
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
        if (usr.getLevel()<3)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.AccessDenyException);
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        List<user> allusers = usi.getAll();
        for (user ussr:allusers)
        {
            privatemessage msg = new privatemessage();
            msg.setSender(usr);
            msg.setReceiver(ussr);
            msg.setText(form.getParameter("text"));
            if ((form.getParameter("imglink")== null)
                    || (form.getParameter("imglink").equals("")));
            {
                msg.setImglink(form.getParameter("imglink"));
            }
            Calendar t = Calendar.getInstance();
            msg.setDate(new Timestamp(t.getTimeInMillis()));
            msg.setViewed(Boolean.FALSE);
            mrp.addPMessage(msg);
        }
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Broadcast successfully exetuted.");
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value="/pmessage/get/sender", method= RequestMethod.POST)
    public  String getbsender(HttpServletRequest form) throws JsonProcessingException
    {
        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("sender")==null)
                || (form.getParameter("sender").equals(""))
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
        user check = usi.getByName(form.getParameter("sender"));
        if (check==null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoUserException);
            rf.setMessage("User with that name does not exists");
            return obm.writeValueAsString(rf);
        }
        if ((!check.equals(usr))&&(usr.getLevel()<3))
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.AccessDenyException);
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        List<privatemessage> sended = check.getSendedmessages();
        if (sended==null) rf.setContent(null);else
        rf.setContent(sended);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Private messages successfully sended.");
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value="/pmessage/get/receiver", method= RequestMethod.POST)
    public  String getbreceiver(HttpServletRequest form) throws JsonProcessingException
    {
        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("receiver")==null)
                || (form.getParameter("receiver").equals(""))
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
        user check = usi.getByName(form.getParameter("receiver"));
        if (check==null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoUserException);
            rf.setMessage("User with that name does not exists");
            return obm.writeValueAsString(rf);
        }
        if ((!check.equals(usr))&&(usr.getLevel()<3))
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.AccessDenyException);
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        List<privatemessage> received = check.getReceivedmessages();
        rf.setContent(received);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Private messages successfully sended.");
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value="/pmessage/get/id", method= RequestMethod.POST)
    public  String getbid(HttpServletRequest form) throws JsonProcessingException
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
        privatemessage msg = mrp.getByPrmid(Integer.parseInt(form.getParameter("message")));
        if (msg == null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoPMessageException);
            rf.setMessage("Message with that id does not exists.");
            return obm.writeValueAsString(rf);
        }
        user check = msg.getReceiver();
        if ((!check.equals(usr))&&(usr.getLevel()<3))
        {
            check = msg.getSender();
            if ((!check.equals(usr))&&(usr.getLevel()<3))
            {
                rf.setError();
                rf.setErrortype(KnownExceptions.AccessDenyException);
                rf.setMessage("You don't have enough rights to do this operation.");
                return obm.writeValueAsString(rf);
            }
        }

        List<privatemessage> lst = new ArrayList<privatemessage>();
        lst.add(msg);
        rf.setContent(lst);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Private message successfully sended.");
        return obm.writeValueAsString(rf);
    }
}
