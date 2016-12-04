package com.Nikas.pojo;

/**
 * Created by Nikas on 04.12.2016.
 */
//generic answer POJO class
//use to provide response to void methods
public class respForm {
    private String status;
    private String errortype;
    private String message;
    public String getStatus(){return status;};
    public String getErrortype(){return errortype;};
    public String getMessage(){return message;};
    public void setStatus(String st){status=st;};
    public void setErrortype(String et){errortype=et;};
    public void setMessage(String m){message=m;};
    public respForm(){};
}
