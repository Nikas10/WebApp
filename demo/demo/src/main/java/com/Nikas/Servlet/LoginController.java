package com.Nikas.Servlet;


import com.Nikas.entity.section;
import com.Nikas.entity.user;
import com.Nikas.pojo.enums.KnownExceptions;
import com.Nikas.pojo.logForm;
import com.Nikas.pojo.respForm;
import com.Nikas.service.impl.sectionServiceImpl;
import com.Nikas.service.impl.userServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
public class LoginController
{
    @Resource(name="UserService")
    userServiceImpl usi;

    @Resource(name="SectionService")
    sectionServiceImpl sectr;


    public logForm lf = new logForm();
    public respForm rf = new respForm();

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public String login(HttpServletRequest loginForm) throws JsonProcessingException //ready
    {
        if (loginForm.getParameter("name")==null) lf.setName(""); else
        lf.setName(loginForm.getParameter("name"));
        if (loginForm.getParameter("pass")==null) lf.setPass(""); else
        lf.setPass(loginForm.getParameter("pass"));
        rf = usi.checkLogindata(lf.getName(),lf.getPass());
        ObjectMapper obm = new ObjectMapper();
        if (rf.getStatus().equals("success"))
        {
            user dbo = usi.getByName(lf.getName());
            List<user> userList;
            userList = new ArrayList<>();
            userList.add(dbo);
            rf.setContent(userList);
            return obm.writeValueAsString(rf);
        }
        else
            return obm.writeValueAsString(rf);
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public String register(HttpServletRequest regForm) throws JsonProcessingException //ready
    {
        if (regForm.getParameter("name") == null) lf.setName("");
        else
            lf.setName(regForm.getParameter("name"));
        if (regForm.getParameter("pass") == null) lf.setPass("");
        else
            lf.setPass(regForm.getParameter("pass"));
        String mail;
        if (regForm.getParameter("mail") == null) mail = "";
        else
            mail = regForm.getParameter("mail");

        user dbo = usi.getByName(lf.getName());
        ObjectMapper obm = new ObjectMapper();
        if (dbo != null) {
            rf.setStatus("error");
            rf.setErrortype("UserExistsError");
            rf.setMessage("User with that name already exists.");
            return obm.writeValueAsString(rf);
        }
        if ((mail.equals("")) || (lf.getPass().equals(""))) {
            rf.setStatus("error");
            rf.setErrortype("NotEnoughDataError");
            rf.setMessage("Some required fields are empty.");
            return obm.writeValueAsString(rf);
        }
        dbo = new user();
        UUID newid = UUID.randomUUID();
        user check = usi.getByMail(mail);
        if (check != null)
        {
            rf.setStatus("error");
            rf.setErrortype("UserExistsError");
            rf.setMessage("User with that mail already exists.");
            return obm.writeValueAsString(rf);
        }
        dbo.setMail(mail);
        dbo.setName(lf.getName());
        dbo.setPassword(lf.getPass());
        dbo.setUid(newid);
        Integer lvl = 1;
        dbo.setLevel(lvl);
        usi.addUser(dbo);
        rf.setStatus("success");
        rf.setErrortype("none");
        rf.setMessage("User successfully registered.");
        return obm.writeValueAsString(rf);
    }



    @RequestMapping(value = "/user/update") //ready
    public String update(HttpServletRequest upForm) throws JsonProcessingException
    {
        ObjectMapper obm = new ObjectMapper();
        if (upForm.getParameter("name")==null) lf.setName(""); else
        lf.setName(upForm.getParameter("name"));
        if (upForm.getParameter("pass")==null) lf.setPass(""); else
        lf.setPass(upForm.getParameter("pass"));
        usi.checkLogindata(lf.getName(),lf.getPass());
        if (rf.getStatus().equals("error"))
        {
           return obm.writeValueAsString(rf);
        }
        user dbo = usi.getByName(lf.getName());
        String nname ;
        if (upForm.getParameter("newpass")==null) nname=""; else
        nname= upForm.getParameter("newname");
        String npass ;
        if (upForm.getParameter("newpass")==null) npass=(""); else
        npass= upForm.getParameter("newpass");
        if (!nname.equals(""))
        {
            user checks = usi.getByName(nname);
            if (checks!=null)
            {
                if (checks.getName().equals(dbo.getName()))
                {
                    dbo.setName(nname);
                }
                else
                {
                    rf.setStatus("error");
                    rf.setErrortype("UserExistsError");
                    rf.setMessage("User with that name already exists.");
                    return obm.writeValueAsString(rf);
                }
            }
            else dbo.setName(nname);
        }
        if (!npass.equals("")) {
            dbo.setPassword(npass);
        }
        if (!upForm.getParameter("mail").equals("")) {
            user check = usi.getByMail(upForm.getParameter("mail"));
            if (check == null)
            {
                rf.setStatus("error");
                rf.setErrortype("UserExistsError");
                rf.setMessage("User with that mail already exists.");
                return obm.writeValueAsString(rf);
            }
            dbo.setMail(upForm.getParameter("mail"));
        }
        dbo.setCountry(upForm.getParameter("country"));
        dbo.setLanguage(upForm.getParameter("language"));
        if (upForm.getParameter("timezone")!=null)
        if (!upForm.getParameter("timezone").equals(""))
        dbo.setTimezone(Integer.parseInt(upForm.getParameter("timezone")));
        dbo.setAbout(upForm.getParameter("about"));
        usi.editUser(dbo);
        rf.setStatus("success");
        rf.setErrortype("none");
        rf.setMessage("User successfully modified.");
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value = "/user/delmod") // ready
    public String moddelete(HttpServletRequest modForm) throws JsonProcessingException
    {
        ObjectMapper obm = new ObjectMapper();
        if (modForm.getParameter("name")==null) lf.setName(""); else
        lf.setName(modForm.getParameter("name")); //moder data
        if (modForm.getParameter("pass")==null) lf.setPass(""); else
        lf.setPass(modForm.getParameter("pass"));
        String del;
        if (modForm.getParameter("delete")==null) del=""; else
        del = modForm.getParameter("delete");
        if ((lf.getName().equals(""))||(lf.getPass().equals(""))||(del.equals("")))
        {
            rf.setStatus("error");
            rf.setErrortype("NotEnoughDataError");
            rf.setMessage("Some required fields are empty.");
            return obm.writeValueAsString(rf);
        }
        usi.checkLogindata(lf.getName(),lf.getPass());
        if (rf.getStatus().equals("error"))
        {
            return obm.writeValueAsString(rf);
        }
        user mod = usi.getByName(lf.getName());
        if (mod.getLevel()<2)
        {
            rf.setStatus("error");
            rf.setErrortype("AccessDenyError");
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        user todelete = usi.getByName(del);
        if (todelete==null)
        {
            rf.setStatus("error");
            rf.setErrortype("NoUserError");
            rf.setMessage("User not found.");
            return obm.writeValueAsString(rf);
        }
        if (todelete.getLevel()>=mod.getLevel())
        {
            rf.setStatus("error");
            rf.setErrortype("AccessDenyError");
            rf.setMessage("User you want to delete is has your or higher level.");
            return obm.writeValueAsString(rf);
        }
        usi.deleteUser(todelete.getUid());
        rf.setStatus("success");
        rf.setErrortype("none");
        rf.setMessage("User successfully deleted.");
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value = "/user/modlevel")// ready
    public String modlevel(HttpServletRequest modForm) throws JsonProcessingException
    {
        ObjectMapper obm = new ObjectMapper();
        if (modForm.getParameter("name")==null) lf.setName(""); else
        lf.setName(modForm.getParameter("name")); //moder data
        if (modForm.getParameter("pass")==null) lf.setPass(""); else
        lf.setPass(modForm.getParameter("pass"));
        String upd;
        if (modForm.getParameter("update")==null) upd=""; else
        upd = modForm.getParameter("update");
        Integer level;
        if ((modForm.getParameter("level").equals(""))||(modForm.getParameter("level")==null))
            level = 0; else level = Integer.parseInt(modForm.getParameter("level"));

        if ((lf.getName().equals(""))||(lf.getPass().equals(""))||(upd.equals("")))
        {
            rf.setStatus("error");
            rf.setErrortype("NotEnoughDataError");
            rf.setMessage("Some required fields are empty.");
            return obm.writeValueAsString(rf);
        }
        usi.checkLogindata(lf.getName(),lf.getPass());
        if (rf.getStatus().equals("error"))
        {
            return obm.writeValueAsString(rf);
        }
        user mod = usi.getByName(lf.getName());
        if (mod.getLevel()<2)
        {
            rf.setStatus("error");
            rf.setErrortype("AccessDenyError");
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        user toupd = usi.getByName(upd);
        if (toupd==null)
        {
            rf.setStatus("error");
            rf.setErrortype("NoUserError");
            rf.setMessage("User not found.");
            return obm.writeValueAsString(rf);
        }
        if (toupd.getLevel()>=mod.getLevel())
        {
            rf.setStatus("error");
            rf.setErrortype("AccessDenyError");
            rf.setMessage("User you want to delete is has your or higher level.");
            return obm.writeValueAsString(rf);
        }
        toupd.setLevel(level);
        usi.editUser(toupd);
        rf.setStatus("success");
        rf.setErrortype("none");
        rf.setMessage("User level successfully modified.");
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value = "/user/delete")// ready
    public String delete  (HttpServletRequest delForm) throws JsonProcessingException
    {
        ObjectMapper obm = new ObjectMapper();
        if (delForm.getParameter("name")==null) lf.setName(""); else
        lf.setName(delForm.getParameter("name"));
        if (delForm.getParameter("pass")==null) lf.setPass(""); else
        lf.setPass(delForm.getParameter("pass"));
        if ((lf.getName().equals(""))||(lf.getPass().equals("")))
        {
            rf.setStatus("error");
            rf.setErrortype("NotEnoughDataError");
            rf.setMessage("Some required fields are empty.");
            return obm.writeValueAsString(rf);
        }
        usi.checkLogindata(lf.getName(),lf.getPass());
        if (rf.getStatus().equals("error"))
        {
            return obm.writeValueAsString(rf);
        }
        user del = usi.getByName(lf.getName());
        usi.deleteUser(del.getUid());
        rf.setStatus("success");
        rf.setErrortype("none");
        rf.setMessage("User successfully deleted.");
        return obm.writeValueAsString(rf);
    }


    @RequestMapping(value="/user/add/curated", method= RequestMethod.POST)
    public  String addcurated(HttpServletRequest form) throws JsonProcessingException
    {
        ObjectMapper obm = new ObjectMapper();
        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("username")==null)
                || (form.getParameter("username").equals(""))
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
        if (check.getLevel()<2)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.AccessDenyException);
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        section sect = sectr.getBySid(Integer.parseInt(form.getParameter("sectid")));
        if (sect==null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoSectionException);
            rf.setMessage("Section with that id does not exists.");
            return obm.writeValueAsString(rf);
        }
        List<section> lst = check.getSections();
        if (lst.contains(sect))
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.UserExistsException);
            rf.setMessage("User with that id already exists in the list.");
            return obm.writeValueAsString(rf);
        }
        lst.add(sect);
        check.setSections(lst);
        usi.editUser(check);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("User successfully modified.");
        return obm.writeValueAsString(rf);
    }


    @RequestMapping(value="/user/get/curated", method= RequestMethod.POST)
    public  String curatedbyuser(HttpServletRequest form) throws JsonProcessingException
    {
        ObjectMapper obm = new ObjectMapper();
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
        List<section> lst = check.getSections();
        rf.setContent(lst);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Sections successfully sended.");
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value="/user/delete/curated", method= RequestMethod.POST)
    public  String deletecurated(HttpServletRequest form) throws JsonProcessingException
    {
        ObjectMapper obm = new ObjectMapper();
        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("username")==null)
                || (form.getParameter("username").equals(""))
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
        section sect = sectr.getBySid(Integer.parseInt(form.getParameter("sectid")));
        if (sect==null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoSectionException);
            rf.setMessage("Section with that id does not exists.");
            return obm.writeValueAsString(rf);
        }
        List<section> lst = check.getSections();
        lst.remove(sect);
        check.setSections(lst);
        usi.editUser(check);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("User successfully modified.");
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value="/user/get/blacklist", method= RequestMethod.POST)
    public  String getblacklisted(HttpServletRequest form) throws JsonProcessingException
    {
        ObjectMapper obm = new ObjectMapper();
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
        List<user> lst = check.getBanned();
        rf.setContent(lst);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Blacklist successfully sended.");
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value="/user/add/blacklist", method= RequestMethod.POST)
    public  String addblacklisted(HttpServletRequest form) throws JsonProcessingException
    {
        ObjectMapper obm = new ObjectMapper();
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
        List<user> banned = usr.getBanned();
        if (banned.contains(check))
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.UserExistsException);
            rf.setMessage("User with that name already exists in the list.");
            return obm.writeValueAsString(rf);
        }
        banned.add(check);
        usr.setBanned(banned);
        usi.editUser(usr);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Blacklist successfully modified.");
        return obm.writeValueAsString(rf);
    }
    @RequestMapping(value="/user/delete/blacklist", method= RequestMethod.POST)
    public  String delfromblacklisted(HttpServletRequest form) throws JsonProcessingException
    {
        ObjectMapper obm = new ObjectMapper();
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
        user check = usi.getByName(form.getParameter("username"));
        if (check==null)
        {
            rf.setError();
            rf.setErrortype(KnownExceptions.NoUserException);
            rf.setMessage("User with that username does not exists.");
            return obm.writeValueAsString(rf);
        }
        List<user> banned = usr.getBanned();
        banned.remove(check);
        usr.setBanned(banned);
        usi.editUser(usr);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Blacklist successfully modified.");
        return obm.writeValueAsString(rf);
    }
}