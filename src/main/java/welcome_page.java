import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = {"/welcome_page"}, loadOnStartup = 2)
public class welcome_page extends HttpServlet {
    private static String firstname;
    private static String lastname;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h2>hello my friend</h2>");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String dbUrl =  System.getenv("JDBC_DATABASE_URL");

        try {
            // Registers the driver
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {}
        Connection conn= null;
        try {
            conn = DriverManager.getConnection(dbUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String uname = (String)request.getAttribute("username");
        try {
            Statement s=conn.createStatement();
            PreparedStatement ps=conn.prepareStatement("select * from doctor_login_info where username=?");
            ps.setString(1,uname);
            ResultSet resultset = ps.executeQuery();
            while(resultset.next()) {
                firstname = resultset.getString("firstname");
                lastname = resultset.getString("lastname");
            }
            resultset.close();
            s.close();
            conn.close();
        }
        catch(Exception e){}
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("Welcome "+firstname+" "+lastname);
        response.sendRedirect("welcome_page");
    }
}
