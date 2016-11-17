package nik;
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

        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:G://JAVA DEV/ForumDB.sqlite";
            Connection conn = DriverManager.getConnection(dbURL);
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
        }
    }
}
