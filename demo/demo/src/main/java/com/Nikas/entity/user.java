package com.Nikas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;
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
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usr",cascade = CascadeType.ALL)
    private List<message> publicmsgs;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receiver",cascade = CascadeType.ALL)
    private List<privatemessage> receivedmessages;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sender",cascade = CascadeType.ALL)
    private List<privatemessage> sendedmessages;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator",cascade = CascadeType.ALL)
    private List<topic> createdtopics;

    @ManyToMany
    @JoinTable(name="curator",schema = "private",
            joinColumns = @JoinColumn(name="uid", referencedColumnName="uid"),
            inverseJoinColumns = @JoinColumn(name="sid", referencedColumnName="sid")
    )
    @JsonIgnore
    private List<section> sections;



    public user(){};
    public user(UUID id, String usr, String pass)
    {
        this.uid = id;
        this.name = usr;
        this.password=pass;
    };

    public List<section> getSections(){return sections;}
    public void setSections(List<section> lst){sections = lst;}
    public List<topic> getCreatedtopics(){return createdtopics;}
    public void setCreatedtopics(List<topic> pm){createdtopics = pm;}
    public List<privatemessage> getReceivedmessages(){return receivedmessages;}
    public void setReceivedmessages(List<privatemessage> pm){receivedmessages = pm;}
    public List<privatemessage> getSendedmessages(){return sendedmessages;}
    public void setSendedmessages(List<privatemessage> pm){sendedmessages = pm;}
    public List<message> getPublicmsgs(){return publicmsgs;}
    public void setPublicmsgs(List<message> msgs){publicmsgs=msgs;}

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
