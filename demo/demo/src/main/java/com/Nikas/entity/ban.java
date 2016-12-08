package com.Nikas.entity;

import com.sun.javafx.beans.IDProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Nikas on 08.12.2016.
 */
@Entity
@Table(name="ban",schema="private")
public class ban implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bid")
    private Integer bid;
    @Column(name="cause")
    private String cause;
    @Column(name="until")
    private Timestamp until;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="modid")
    private user moderator;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="banid")
    private user userban;

    public Integer getBid(){return bid;}
    public void setBid(Integer id) {bid =id;}
    public String getCause(){return cause;}
    public void setCause(String c){cause=c;}
    public Timestamp getUntil(){return until;}
    public void setUntil(Timestamp time){until=time;}
    public user getModerator(){return moderator;}
    public void setModerator(user usr){moderator=usr;}
    public user getUserban(){return userban;}
    public void setUserban(user usr){userban = usr;}
}
