package nik;
import org.hibernate.Session;
import util.HibernateSessionFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Nikas on 09.11.2016.
 */

@WebServlet("/hello")
public class MyServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public MyServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println("<h1>Hello Servlet 1111111</h1>");

        /*try {
            Class.forName("org.postgresql.Driver");
            String dbURL = "jdbc:postgresql://localhost:5432/ForumDB";
            String user = "admin";
            String pass = "1q2w3e";
            Connection conn = DriverManager.getConnection(dbURL, user, pass);
            if (conn != null) {
                out.println("Connected to the database");
                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
                out.println("Driver name: " + dm.getDriverName());
                out.println("Driver version: " + dm.getDriverVersion());
                out.println("Product name: " + dm.getDatabaseProductName());
                out.println("Product version: " + dm.getDatabaseProductVersion());
                conn.close();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace(out);
        } catch (SQLException e) {
            e.printStackTrace(out);
        }*/
        Session session = HibernateSessionFactory.getSessionFactory().openSession();

        session.beginTransaction();
        out.println("NO error message");
        session.getTransaction().commit();

        session.close();
    }
}
