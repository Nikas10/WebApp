package com.Nikas.entity;

/**
 * Created by Nikas on 02.12.2016.
 */
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="section",schema = "private")
public class section implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="sid")
    private Integer sid;

    @Column(name="parent")
    private Integer parent;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;


    public Integer getSid(){return sid;}
    public void setSid(Integer id){sid=id;}
    public Integer getParent(){return parent;}
    public void setParent(Integer par){parent=par;}
    public String getName(){return name;}
    public void setName(String nam){name=nam;}
    public String getDescription(){return description;}
    public void setDescription(String desc){description=desc;}

}
