package com.Nikas.Servlet.utils;

import com.Nikas.entity.user;
import com.Nikas.pojo.logForm;
import com.Nikas.pojo.respForm;
import com.Nikas.service.impl.userServiceImpl;

import javax.annotation.Resource;

/**
 * Created by Nikas on 04.12.2016.
 */
public class Helper {

    @Resource(name="UserService")
    static userServiceImpl usi = new userServiceImpl();

    public static respForm rf = new respForm();

    public static respForm checkLogindata(String name, String pass) //ready
    {
        user dbo = usi.getByName(name);
        if (dbo!=null)
        {
            if (!pass.equals(dbo.getPassword()))
            {
                rf.setStatus("error");
                rf.setErrortype("LoginError");
                rf.setMessage("Your password or username is invalid.");
                return rf;
            }
            else
            {
                rf.setStatus("success");
                rf.setErrortype("none");
                rf.setMessage("Login successful.");
                return rf;
            }
        }
        {
            rf.setStatus("error");
            rf.setErrortype("NoUserError");
            rf.setMessage("User not found. Check your username or register.");
            return rf;
        }
    };
}
