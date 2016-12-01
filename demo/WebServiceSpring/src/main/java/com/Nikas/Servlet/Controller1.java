package com.Nikas.Servlet;


import com.Nikas.entity.user;
import com.Nikas.repo.UserRepo;
import com.Nikas.service.impl.userServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class Controller1
{

    @Resource(name="UserService")
    userServiceImpl usi;

    @RequestMapping("/helloworld")
    public String print(@RequestParam(value="name", required=false, defaultValue="sampleuser") String name,@RequestParam(value="pass", required=false, defaultValue="1q2w3e") String pass)
    {
        System.out.println(name);
        System.out.println(pass);
        user dbo = new user();
        dbo = usi.getByName(name);
        ObjectMapper obm = new ObjectMapper();
        String response="";
        try {
            response = obm.writeValueAsString(dbo);
        }
        catch (JsonProcessingException ex)
        {
            ex.printStackTrace();
        }
        if (dbo!=null) return response; else
        return "No data allowed";
    }
}