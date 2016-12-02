package com.Nikas.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Nikas on 02.12.2016.
 */
@Entity
@Table(name="topic",schema = "private")
public class topic implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="tid")
    private Integer tid;

    @Column(name="sid")
    private Integer sid;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="date")
    private Timestamp date;

    @Column(name="status")
    private Boolean status;

    public Integer getTid(){return tid;}
    public void setTid(Integer id){tid=id;}
    public Integer getSid(){return sid;}
    public void setSid(Integer id){sid=id;}
    public Timestamp getDate(){return date;}
    public void setParent(Timestamp da){date=da;}
    public String getName(){return name;}
    public void setName(String nam){name=nam;}
    public String getDescription(){return description;}
    public void setDescription(String desc){description=desc;}
    public Boolean getStatus(){return status;}
    public void setStatus(Boolean st){status=st;}
}