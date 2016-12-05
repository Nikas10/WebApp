package com.Nikas.Servlet;

import com.Nikas.entity.section;
import com.Nikas.entity.user;
import com.Nikas.pojo.logForm;
import com.Nikas.pojo.respForm;
import com.Nikas.service.impl.sectionServiceImpl;
import com.Nikas.service.impl.userServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikas on 04.12.2016.
 */
@RestController
public class SectionController {

    @Resource(name = "SectionService")
    sectionServiceImpl sectr;
    @Resource(name="UserService")
    userServiceImpl usi;
    ObjectMapper obm = new ObjectMapper();

    public logForm lf = new logForm();
    public respForm rf = new respForm();

    @RequestMapping(value ="/section/add", method = RequestMethod.POST)
    public String addsection(HttpServletRequest form) throws JsonProcessingException {

        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("sectname")==null)
                || (form.getParameter("sectname").equals("")))
        {
            rf.setError();
            rf.setErrortype("NotEnoughDataError");
            rf.setMessage("Some required fields are empty");
            return obm.writeValueAsString(rf);
        }
        lf.setName(form.getParameter("name"));
        lf.setPass(form.getParameter("pass"));
        rf= usi.checkLogindata(lf.getName(),lf.getPass());
        if (rf.getStatus().equals("error"))
        {
            return obm.writeValueAsString(rf);
        }
        user usr = usi.getByName(lf.getName());
        if (usr.getLevel()<2)
        {
            rf.setError();
            rf.setErrortype("AccessDenyError");
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        section newsect = new section();
        newsect.setName(form.getParameter("sectname"));
        section check;
        check  = sectr.getByName(newsect.getName());
        if (check!=null)
        {
            rf.setError();
            rf.setErrortype("SectionExistsError");
            rf.setMessage("Section with the same name exists.");
            return obm.writeValueAsString(rf);
        }
        if ((form.getParameter("desc")==null)||(form.getParameter("desc").equals("")))
        {
            newsect.setDescription("");
        }else
            newsect.setDescription(form.getParameter("desc"));
        if ((form.getParameter("parent")==null)||(form.getParameter("parent").equals("")))
        newsect.setParsect(null);
        else
            {
                Integer id = Integer.parseInt(form.getParameter("parent"));
                check = sectr.getBySid(id);
                if (check==null)
                {
                    rf.setError();
                    rf.setErrortype("NoParentError");
                    rf.setMessage("Parent is missing.");
                    return obm.writeValueAsString(rf);
                }
                newsect.setParsect(check);
            }
        sectr.addSection(newsect);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Section successfully added.");
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value ="/section/delete", method = RequestMethod.POST)
    public String deletesection(HttpServletRequest form) throws JsonProcessingException {

        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("sectname")==null)
                || (form.getParameter("sectname").equals("")))
        {
            rf.setError();
            rf.setErrortype("NotEnoughDataError");
            rf.setMessage("Some required fields are empty");
            return obm.writeValueAsString(rf);
        }
        lf.setName(form.getParameter("name"));
        lf.setPass(form.getParameter("pass"));
        rf= usi.checkLogindata(lf.getName(),lf.getPass());
        if (rf.getStatus().equals("error"))
        {
            return obm.writeValueAsString(rf);
        }
        user usr = usi.getByName(lf.getName());
        if (usr.getLevel()<2)
        {
            rf.setError();
            rf.setErrortype("AccessDenyError");
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        section check;
        check  = sectr.getByName(form.getParameter("sectname"));
        if (check==null)
        {
            rf.setError();
            rf.setErrortype("NoSectionError");
            rf.setMessage("Section with this name does not exists.");
            return obm.writeValueAsString(rf);
        }
        sectr.deleteSection ( check.getSid() );
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Section successfully removed.");
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value ="/section/getone", method = RequestMethod.POST)
    public String getsection(HttpServletRequest form) throws JsonProcessingException
    {
        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("sectname")==null)
                || (form.getParameter("sectname").equals("")))
        {
            rf.setError();
            rf.setErrortype("NotEnoughDataError");
            rf.setMessage("Some required fields are empty");
            return obm.writeValueAsString(rf);
        }
        lf.setName(form.getParameter("name"));
        lf.setPass(form.getParameter("pass"));
        rf= usi.checkLogindata(lf.getName(),lf.getPass());
        if (rf.getStatus().equals("error"))
        {
            return obm.writeValueAsString(rf);
        }
        user usr = usi.getByName(lf.getName());
        if (usr.getLevel()<1)
        {
            rf.setError();
            rf.setErrortype("AccessDenyError");
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        section check;
        check  = sectr.getByName(form.getParameter("sectname"));
        if (check==null)
        {
            rf.setError();
            rf.setErrortype("NoSectionError");
            rf.setMessage("Section with this name does not exists.");
            return obm.writeValueAsString(rf);
        }
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Section successfully sended.");
        List<section> cont = new ArrayList<section>();
        cont.add(check);
        rf.setContent(cont);
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value ="/section/getby", method = RequestMethod.POST)
    public String getsections(HttpServletRequest form) throws JsonProcessingException {

        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                )
        {
            rf.setError();
            rf.setErrortype("NotEnoughDataError");
            rf.setMessage("Some required fields are empty");
            return obm.writeValueAsString(rf);
        }
        lf.setName(form.getParameter("name"));
        lf.setPass(form.getParameter("pass"));
        rf= usi.checkLogindata(lf.getName(),lf.getPass());
        if (rf.getStatus().equals("error"))
        {
            return obm.writeValueAsString(rf);
        }
        user usr = usi.getByName(lf.getName());
        if (usr.getLevel()<1)
        {
            rf.setError();
            rf.setErrortype("AccessDenyError");
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        List<section> check;
        if ((form.getParameter("parent")==null) || (form.getParameter("parent").equals("")))
        {
            check  = sectr.getByParsect(null);
        }else
        check  = sectr.getByParsect(sectr.getBySid(Integer.parseInt(form.getParameter("parent"))));

        if (check==null)
        {
            rf.setError();
            rf.setErrortype("NoSectionsError");
            rf.setMessage("No section with that parent found.");
            return obm.writeValueAsString(rf);
        }
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Sections successfully sended.");
        rf.setContent(check);
        return obm.writeValueAsString(rf);
    }

    @RequestMapping(value ="/section/edit", method = RequestMethod.POST)
    public String editsection(HttpServletRequest form) throws JsonProcessingException {

        if      (  (form.getParameter("name") == null)
                || (form.getParameter("name").equals(""))
                || (form.getParameter("pass") == null)
                || (form.getParameter("pass").equals(""))
                || (form.getParameter("sectname")==null)
                || (form.getParameter("sectname").equals("")))
        {
            rf.setError();
            rf.setErrortype("NotEnoughDataError");
            rf.setMessage("Some required fields are empty");
            return obm.writeValueAsString(rf);
        }
        lf.setName(form.getParameter("name"));
        lf.setPass(form.getParameter("pass"));
        rf= usi.checkLogindata(lf.getName(),lf.getPass());
        if (rf.getStatus().equals("error"))
        {
            return obm.writeValueAsString(rf);
        }
        user usr = usi.getByName(lf.getName());
        if (usr.getLevel()<2)
        {
            rf.setError();
            rf.setErrortype("AccessDenyError");
            rf.setMessage("You don't have enough rights to do this operation.");
            return obm.writeValueAsString(rf);
        }
        section check;
        check = sectr.getByName(form.getParameter("sectname"));
        if (check==null)
        {
            rf.setError();
            rf.setErrortype("NoSectionError");
            rf.setMessage("Section with the same name does not exists.");
            return obm.writeValueAsString(rf);
        }
        if ((form.getParameter("newname")!=null)&&(!form.getParameter("newname").equals(""))) {
            section cc = sectr.getByName(form.getParameter("newname"));
            if (cc!=null)
            {
                rf.setError();
                rf.setErrortype("SectionExistsError");
                rf.setMessage("Section with the same name already exists.");
                return obm.writeValueAsString(rf);
            }
            else
                check.setName(form.getParameter("newname"));
        }
        if ((form.getParameter("desc")==null)||(form.getParameter("desc").equals("")))
        {
            check.setDescription("");
        }else
            check.setDescription(form.getParameter("desc"));
        if ((form.getParameter("parent")==null)||(form.getParameter("parent").equals("")))
            check.setParsect(null);
        else
        {
            Integer id = Integer.parseInt(form.getParameter("parent"));
            check = sectr.getBySid(id);
            if (check==null)
            {
                rf.setError();
                rf.setErrortype("NoParentError");
                rf.setMessage("Parent is missing.");
                return obm.writeValueAsString(rf);
            }
            check.setParsect(check);
        }
        if ((form.getParameter("desc")==null)||(form.getParameter("desc").equals("")))
        {check.setDescription("");}else
            check.setDescription(form.getParameter("desc"));

        sectr.editSection(check);
        rf.setSuccess();
        rf.setErrortype("none");
        rf.setMessage("Section successfully edited.");
        return obm.writeValueAsString(rf);
    }
}
