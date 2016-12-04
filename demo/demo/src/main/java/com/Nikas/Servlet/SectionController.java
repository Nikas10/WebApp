package com.Nikas.Servlet;

import com.Nikas.entity.user;
import com.Nikas.pojo.logForm;
import com.Nikas.pojo.respForm;
import com.Nikas.service.impl.sectionServiceImpl;
import com.Nikas.service.impl.userServiceImpl;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by Nikas on 04.12.2016.
 */
@RestController
public class SectionController {
    @Resource(name = "SectionService")
    sectionServiceImpl sectR;
    @Resource(name="UserService")
    userServiceImpl usi;

    public logForm lf = new logForm();
    public respForm rf = new respForm();
    public respForm checkLogindata(String name, String pass) //ready
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
