package com.Nikas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 * Created by Nikas on 02.12.2016.
 */
@Entity
@Table(name="topic",schema = "private")
public class topic implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="tid")
    private Integer tid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="sid", nullable = false)
    @JsonIgnore
    private section sectid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="uid", nullable = false)
    private user creator;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="date")
    private Timestamp date;

    @Column(name="status")
    private Short status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tpc",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<message> msgs;

    public user getCreator(){return creator;}
    public void setCreator(user n){creator=n;}

    public List<message> getMsgs(){return msgs;}
    public void setMsgs(List<message> msg){msgs=msg;}

    public Integer getTid(){return tid;}
    public void setTid(Integer id){tid=id;}

    public section getSectid(){return sectid;}
    public void setSectid(section id){sectid=id;}

    public Timestamp getDate(){return date;}
    public void setDate(Timestamp da){date=da;}

    public String getName(){return name;}
    public void setName(String nam){name=nam;}

    public String getDescription(){return description;}
    public void setDescription(String desc){description=desc;}

    public Short getStatus(){return status;}
    public void setStatus(Short st){status=st;}
}