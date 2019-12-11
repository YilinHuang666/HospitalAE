import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/timetable_page", loadOnStartup = 1)

public class timetable_page extends HttpServlet {
    private static String reqBody;
    private String firstname;
    private String lastname;
    private final static String dbUrl = System.getenv("JDBC_DATABASE_URL");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        PrintWriter out=response.getWriter();
        out.println("<h2>Timetable</h2>");

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
        String rawTimetable = "";
        try{
            Statement s = conn.createStatement();
            String sqlcom="SELECT * from doctor_login_info where firstname='"+firstname+"' and lastname='"+lastname+"';";
             ResultSet resultset = s.executeQuery(sqlcom);
            while (resultset.next()){
                rawTimetable = resultset.getString("timetable");
            }
            String[] arrOfTb = rawTimetable.split(" ");
            for (String a: arrOfTb){
                TBtowrite tb = new TBtowrite(a);
                out.println("<h2>"+tb.getTB()+" "+tb.getTS()+"</h2>");
            }
            s.close();
            resultset.close();
            conn.close();

        }catch(Exception e){}
        firstname=""; lastname="";

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out=response.getWriter();
        Cookie[] cookies = request.getCookies(); //get login doctor name from welcome page
        if (cookies != null){
            for (Cookie cookie: cookies){
                if (cookie.getName().equals("firstname")) firstname = cookie.getValue();
                if (cookie.getName().equals("lastname")) lastname = cookie.getValue();
            }
        }
        Cookie firstname_remove = new Cookie("firstname","");
        Cookie lastname_remove = new Cookie("lastname","");
        firstname_remove.setMaxAge(0); lastname_remove.setMaxAge(0);
        response.addCookie(firstname_remove); response.addCookie(lastname_remove);
        response.sendRedirect("timetable_page");
    }
}
