package com.Nikas.utils;

import com.Nikas.entity.user;
import com.Nikas.service.impl.userServiceImpl;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Created by Nikas on 02.12.2016.
 */
public class Logger {

    @Resource(name="UserService")
    userServiceImpl usi;

    private UUID uuid;
    private String name;
    private String pass;
    public Logger(){};
    public Logger(UUID id, String n, String p)
    {
        uuid=id;
        name=n;
        pass=p;
    }
    public UUID getUuid()
    {return uuid;}
    public void setUuid(UUID id)
    {
        uuid=id;
    }
    public String getName()
    {return name;}
    public void setName(String n)
    {
        name=n;
    }
    public String getPass()
    {return pass;}
    public void setPass(String p)
    {
        pass=p;
    }
    public user check() {
        user usr = new user();

        if (getUuid() != null) {
            usr = usi.getByUid(uuid);
            if (usr.getUid()==uuid)
                if (usr.getName().equals(name))
                    if (usr.getPassword().equals(pass))
                        return usr;
            else return null;
            return null;
        }
        if (!getName().equals("")) {
            usr = usi.getByName(name);
            if (usr.getUid()==uuid)
                if (usr.getName().equals(name))
                    if (usr.getPassword().equals(pass))
                        return usr;
                    else return null;
            return null;
        }
        return null;
    }
    public user checkreg() //check for registration, no pass req
    {
        user usr = new user();
        if (uuid != null) {
            usr = usi.getByUid(uuid);
            if (usr.getUid()==uuid)
                if (usr.getName().equals(name))
                        return usr;
                    else return null;
            return null;
        } else if (!name.equals("")) {
            usr = usi.getByName(name);
            if (usr.getUid()==uuid)
                if (usr.getName().equals(name))
                        return usr;
                    else return null;
            return null;
        }
        return null;
    }
}
