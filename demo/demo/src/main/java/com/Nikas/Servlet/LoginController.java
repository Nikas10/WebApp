package com.Nikas.Servlet;


import com.Nikas.entity.user;
import com.Nikas.service.impl.userServiceImpl;
import com.Nikas.utils.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.UUID;


@RestController
public class LoginController
{

    @Resource(name="UserService")
    userServiceImpl usi;

    public Logger checker = new Logger();

    @RequestMapping("/login")
    public String login(@RequestParam(value="name", required=false, defaultValue="sampleuser") String name,@RequestParam(value="pass", required=false, defaultValue="1q2w3e") String pass)
    {
        checker.setName(name);
        checker.setPass(pass);
        System.out.println(checker.getName());
        System.out.println(checker.getPass());
        user dbo = usi.getByName(name);
        if (dbo!=null)
        {
            ObjectMapper obm = new ObjectMapper();
            String response = "";
            try {
                response = obm.writeValueAsString(dbo);
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
            if (dbo != null) return response;
            else
                return "{}";
        }
        else return "{}";
    }
    @RequestMapping(value = "/reguser")//ADD POST LATER!
    public String register(@RequestParam(value="name", required=false, defaultValue="sampleuser") String name,@RequestParam(value="pass", required=false, defaultValue="1q2w3e") String pass)
    {
        String response;
        checker.setName(name);
        checker.setPass(pass);
        user dbo = usi.getByName(name);
        if (dbo!=null) return "User already exists!";
        else
        {
            UUID newid;
            dbo.setLevel(1);
            newid=UUID.randomUUID();
            dbo.setUid(newid);
            usi.addUser(dbo);
            return "success!";
        }
    }

    @RequestMapping(value = "/upuser") //ADD POST LATER!
    public String update(@RequestParam(value="uuid", required=false, defaultValue="") String uuid,
                         @RequestParam(value="name", required=false, defaultValue="") String name,
                         @RequestParam(value="pass", required=false, defaultValue="") String pass,
                         @RequestParam(value="mail", required=false, defaultValue="") String mail,
                         @RequestParam(value="coutry", required=false, defaultValue="") String country,
                         @RequestParam(value="time", required=false, defaultValue="") Integer timezone,
                         @RequestParam(value="lang", required=false, defaultValue="") String language,
                         @RequestParam(value="about", required=false, defaultValue="") String about)
    {
        user dbo = new user();
        dbo = usi.getByUid(UUID.fromString(uuid));
        if (dbo==null) return "No such user!";
        user checks = usi.getByName(name);
        if ((checks!=null)&&(checks.getName().equals(name)))return "User with that name exists!";
        else
        {
           if (!name.equals(""))dbo.setName(name);
           if (!pass.equals(""))dbo.setPassword(pass);
            dbo.setMail(mail);
            dbo.setCountry(country);
            dbo.setTimezone(timezone);
            dbo.setLanguage(language);
            dbo.setAbout(about);
            usi.editUser(dbo);
            return "Success!";
        }
    }

    @RequestMapping(value = "/deluser")// ADD POST LATER
    public void moddelete(@RequestParam(value="name", required=true, defaultValue="") String name, @RequestParam(value="uuid", required=true, defaultValue="") String uuid)
    {
        //checker.setName(name);
        user dbo = new user();
        user todel = new user();
        dbo = usi.getByUid(UUID.fromString(uuid));
        todel=usi.getByName(name);
        if ((dbo.getLevel()>1)&&(todel.getLevel()<2))
            if (name.equals(todel.getName()))
            {
                usi.deleteUser(todel.getUid());return;}else
                    return;
    }

    @RequestMapping(value = "/delself")// ADD POST LATER
    public void delete(@RequestParam(value="name", required=true, defaultValue="") String name, @RequestParam(value="pass", required=true, defaultValue="") String pass)
    {
        user dbo = new user();
        dbo = usi.getByName(name);
        if (dbo.getName().equals(name))
        if (dbo.getPassword().equals(pass)){
                usi.deleteUser(dbo.getUid());return;}else
                    return;
    }

}