package com.Nikas.entity;

/**
 * Created by Nikas on 03.12.2016.
 */
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.InterruptedIOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;
import java.util.StringJoiner;
import javax.persistence.*;

@Entity
@Table(name="message",schema="private")
public class message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pmid")
    private Integer pmid;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="uid",nullable = false)
    private user usr;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name="tid",nullable = false)
    private topic tpc;

    @Column(name="text")
    private String text;

    @Column(name="imglink")
    private String imglink;

    @Column(name="date")
    private Timestamp date;

    public Integer getPmid(){return pmid;}
    public void setPmid(Integer pmi){pmid=pmi;}
    public user getUsr(){return usr;}
    public void setUsr(user us){usr=us;}
    public topic getTpc(){return tpc;}
    public void setTpc(topic tp){tpc=tp;}
    public String getText(){return text;}
    public void setText(String txt){text=txt;}
    public String getImglink(){return imglink;}
    public void setImglink(String img){imglink=img;}
    public Timestamp getDate(){return date;}
    public void setDate(Timestamp ts){date=ts;}

}
