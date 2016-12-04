package com.Nikas.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Nikas on 03.12.2016.
 */
@Entity
@Table(name="privatemessage",schema="private")
public class privatemessage {


        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "pmid")
        private Integer pmid;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name="recid",nullable = false)
        private user receiver;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name="sendid",nullable = false)
        private user sender;

        @Column(name="text")
        private String text;

        @Column(name="imglink")
        private String imglink;

        @Column(name="date")
        private Timestamp date;

        @Column(name="viewed")
        private Boolean viewed;

        public Boolean getViewed(){return viewed;}
        public void setViewed(Boolean bl){viewed=bl;}
        public Integer getPmid(){return pmid;}
        public void setPmid(Integer pmi){pmid=pmi;}
        public user getReceiver(){return receiver;}
        public void setReceiver(user us){receiver=us;}
        public user getSender(){return sender;}
        public void setSender(user tp){sender=tp;}

        public String getText(){return text;}
        public void setText(String txt){text=txt;}
        public String getImglink(){return imglink;}
        public void setImglink(String img){imglink=img;}
        public Timestamp getDate(){return date;}
        public void setDate(Timestamp ts){date=ts;}



}
