package com.Nikas.entity;

/**
 * Created by Nikas on 02.12.2016.
 */
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name="section",schema = "private")
public class section implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="sid")
    private Integer sid;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="parent")
    @JsonIgnore
    private section parsect;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sectid", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<topic> topics;

    @OneToMany(fetch=FetchType.LAZY, mappedBy = "parsect", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<section> sections;

    public List<section> getSections(){return sections;}
    public void setSections(List<section> scs){sections=scs;}
    public List<topic> getTopics(){return topics;}
    public void setTopics(List<topic> tpc){topics=tpc;}
    public Integer getSid(){return sid;}
    public void setSid(Integer id){sid=id;}
    public section getParsect(){return parsect;}
    public void setParsect(section par){parsect=par;}
    public String getName(){return name;}
    public void setName(String nam){name=nam;}
    public String getDescription(){return description;}
    public void setDescription(String desc){description=desc;}

}
