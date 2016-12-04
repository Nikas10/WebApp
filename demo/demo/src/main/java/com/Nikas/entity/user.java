package com.Nikas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
/**
 * Created by Nikas on 01.12.2016.
 */
@Entity
@Table(name = "user", schema = "private")
public class user implements Serializable {
    @Id
    @Column(name = "uid")
    private UUID uid;
    @Column (name = "name")
    private String name;
    @Column (name = "password")
    private String password;
    @Column (name = "level")
    private Integer level;
    @Column (name = "mail")
    private String mail;
    @Column (name = "country")
    private String country;
    @Column (name = "timezone")
    private Integer timezone;
    @Column (name = "language")
    private String language;
    @Column (name = "about")
    private String about;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usr")
    private Set<message> publicmsgs;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receiver")
    private Set<privatemessage> receivedmessages;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sender")
    private Set<privatemessage> sendedmessages;

    public user(){};
    public user(UUID id, String usr, String pass)
    {
        this.uid = id;
        this.name = usr;
        this.password=pass;
    };
    public Set<privatemessage> getReceivedmessages(){return receivedmessages;}
    public void setReceivedmessages(Set<privatemessage> pm){receivedmessages = pm;}
    public Set<privatemessage> getSendedmessages(){return sendedmessages;}
    public void setSendedmessages(Set<privatemessage> pm){sendedmessages = pm;}
    public Set<message> getPublicmsgs(){return publicmsgs;}
    public void setPublicmsgs(Set<message> msgs){publicmsgs=msgs;}

    public UUID getUid()
    {return uid;}
    public void setUid(UUID iid)
    {uid=iid;}


    public String getName()
    {return name;}
    public void setName(String usr)
    {name=usr;}


    public  String getPassword()
    {return password;}
    public void setPassword(String pass)
    {password=pass;}


    public Integer getLevel()
    {return level;}
    public void setLevel(Integer lvl)
    {level = lvl;}


    public String getMail()
    {return mail;}
    public void setMail(String mail)
    {this.mail=mail;}


    public String getCountry()
    {return country;}
    public void setCountry(String ctr)
    {country=ctr;}


    public String getLanguage()
    {return language;}
    public void setLanguage(String lng)
    {language=lng;}


    public Integer getTimezone()
    {return timezone;}
    public void setTimezone(Integer tmz)
    {timezone=tmz;}


    public String getAbout()
    {return about;}
    public void setAbout(String About)
    {about = About;}

}
