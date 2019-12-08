import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/mypatients_page", loadOnStartup = 1)

public class mypatients_page extends HttpServlet {
    private static String reqBody;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String dbUrl =  System.getenv("JDBC_DATABASE_URL");
        response.setContentType("text/html");
        PrintWriter out=response.getWriter();
        out.println("<h2>My Patients</h2>");
        out.println("<h2>"+reqBody+"</h2>");
        try {
            // Registers the driver
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {}
        Connection conn=null;
        try {
            conn= DriverManager.getConnection(dbUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            Statement s = conn.createStatement();
            //PreparedStatement ps = conn.prepareStatement("SELECT ")
        }catch(Exception e){}
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        reqBody=request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        response.sendRedirect("mypatients_page");
    }
}
