package com.Nikas.Servlet;

import com.Nikas.entity.ban;
import com.Nikas.entity.user;
import com.Nikas.pojo.enums.KnownExceptions;
import com.Nikas.pojo.logForm;
import com.Nikas.pojo.respForm;
import com.Nikas.service.impl.banServiceImpl;
import com.Nikas.service.impl.userServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
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
public class BanController {
    @Resource(name = "BanService")
    banServiceImpl bsi;
    @Resource(name="UserService")
    userServiceImpl usi;

    ObjectMapper obm = new ObjectMapper();

    public logForm lf = new logForm();
    public respForm rf = new respForm();

    @RequestMapping(value = "/ban/add", method = RequestMethod.POST)
    public String add(HttpServletRequest form) throws JsonProcessingException
    {
        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("username")==null)
                || (form.getParameter("username").equals(""))
                || (form.getParameter("until")==null)
                || (form.getParameter("until").equals(""))
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
        ban bn = new ban();
        if ((form.getParameter("cause")!=null)
                && (!form.getParameter("cause").equals("")))
        bn.setCause(form.getParameter("cause"));
        bn.setModerator(usr);
        bn.setUserban(check);
        Timestamp tms;
        tms = Timestamp.valueOf(form.getParameter("until"));
        bn.setUntil(tms);
        bsi.addBan(bn);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("User successfully banned.");
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value = "/ban/delete", method = RequestMethod.POST)
    public String del(HttpServletRequest form) throws JsonProcessingException
    {
        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("ban")==null)
                || (form.getParameter("ban").equals(""))
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
        ban bn = bsi.getByBid(Integer.parseInt(form.getParameter("ban")));
        if (bn==null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoBanException);
            rf.setMessage("Ban with this id does not exists.");
            return obm.writeValueAsString(rf);
        }
        user check = bn.getModerator();
        if((check.getLevel()>usr.getLevel())&&(!check.equals(usr)))
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.AccessDenyException);
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        bsi.delBan(bn);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Ban successfully removed.");
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value = "/ban/get/id", method = RequestMethod.POST)
    public String getid(HttpServletRequest form) throws JsonProcessingException
    {
        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("ban")==null)
                || (form.getParameter("ban").equals(""))
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
        ban bn = bsi.getByBid(Integer.parseInt(form.getParameter("ban")));
        if (bn==null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoBanException);
            rf.setMessage("Ban with this id does not exists.");
            return obm.writeValueAsString(rf);
        }

        List<ban> lst = new ArrayList<>();
        lst.add(bn);
        rf.setContent(lst);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Banlist successfully sended.");
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value = "/ban/get/mod", method = RequestMethod.POST)
    public String getmod(HttpServletRequest form) throws JsonProcessingException
    {
        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("mod")==null)
                || (form.getParameter("mod").equals(""))
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
        user mod = usi.getByName(form.getParameter("mod"));
        if (mod==null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoUserException);
            rf.setMessage("User with that id does not exists.");
            return obm.writeValueAsString(rf);
        }

        List<ban> bn = mod.getMybans();
        if (bn==null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoBanException);
            rf.setMessage("Bans with that moderator do not exist.");
            return obm.writeValueAsString(rf);
        }

        rf.setContent(bn);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Banlist successfully sended.");
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value = "/ban/get/user", method = RequestMethod.POST)
    public String getuser(HttpServletRequest form) throws JsonProcessingException
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
        if (usr.getLevel()<1)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.AccessDenyException);
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        user mod = usi.getByName(form.getParameter("username"));
        if (mod==null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoUserException);
            rf.setMessage("User with that id does not exists.");
            return obm.writeValueAsString(rf);
        }

        List<ban> bn = mod.getInbans();
        if (bn==null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoBanException);
            rf.setMessage("Bans with that user do not exist.");
            return obm.writeValueAsString(rf);
        }

        rf.setContent(bn);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Banlist successfully sended.");
        return obm.writeValueAsString(rf);
    }
}
