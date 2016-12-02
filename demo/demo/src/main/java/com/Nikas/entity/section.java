package com.Nikas.entity;

/**
 * Created by Nikas on 02.12.2016.
 */
import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name="section",schema = "private")
public class section implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="sid")
    private Integer sid;

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="parent")
    private section parsect;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sectid")
    private Set<topic> topics;

    @OneToMany(fetch=FetchType.LAZY, mappedBy = "parsect")
    private Set<section> sections;

    public Set<section> getSections(){return sections;}
    public void setSections(Set<section> scs){sections=scs;}
    public Set<topic> getTopics(){return topics;}
    public void setTopics(Set<topic> tpc){topics=tpc;}
    public Integer getSid(){return sid;}
    public void setSid(Integer id){sid=id;}
    public section getParsect(){return parsect;}
    public void setParsect(section par){parsect=par;}
    public String getName(){return name;}
    public void setName(String nam){name=nam;}
    public String getDescription(){return description;}
    public void setDescription(String desc){description=desc;}

}
