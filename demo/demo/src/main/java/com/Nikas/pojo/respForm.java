package com.Nikas.pojo;

import com.Nikas.pojo.enums.KnownExceptions;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Nikas on 04.12.2016.
 */
//generic answer POJO class
//use to provide response to void methods
public class respForm {
    private String status;
    private String errortype;
    private String message;
    private List<? extends Serializable> content;

    public String getStatus(){return status;};
    public String getErrortype(){return errortype;};
    public String getMessage(){return message;};
    public void setStatus(String st){status=st;};
    public void setErrortype(String et){errortype=et;};
    public void setErrortype(KnownExceptions et){errortype=et.toString();};
    public void setMessage(String m){message=m;};
    public List<? extends Serializable> getContent(){return content;}
    public void setContent(List<? extends Serializable> cont){content=cont;}
    public respForm(){};

    public void setError(){status = "error";}
    public void setSuccess(){status="success";}
}
