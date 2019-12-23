import Functions.ShiftsRemain;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/workload_page", loadOnStartup = 1)

public class workload_page extends HttpServlet {
    private static String reqBody;
    private String firstname;
    private String lastname;
    private static int shifts = 0;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String dbUrl =  System.getenv("JDBC_DATABASE_URL");
        response.setContentType("text/html");
        PrintWriter out=response.getWriter();
        out.println("<h2>My Work Load</h2>");
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
            String sqlcom="SELECT * from doctor_login_info where firstname='"+firstname+"' and lastname='"+lastname+"';";
             ResultSet resultset = s.executeQuery(sqlcom);
            while (resultset.next()){
                shifts = Integer.parseInt(resultset.getString("workload"));
                out.println("<h2> You have "+shifts+" shifts this week.</h2>");
            }
            sqlcom="SELECT * from doctor_login_info where firstname='"+firstname+"' and lastname='"+lastname+"';";
            resultset = s.executeQuery(sqlcom);
            while (resultset.next()){
                String rawTimetable = resultset.getString("timetable");
                String[] arrOfTb = rawTimetable.split(" ");
                ShiftsRemain sr = new ShiftsRemain(arrOfTb);
                if(sr.isOnDuty()==true)
                    out.println("<h2> You are currently on Duty!</h2>");
                out.println("<h2> You have " + sr.getRemainShifts() + " shifts left.</h2>");
            }
            s.close();
            resultset.close();
            conn.close();
        }catch(Exception e){}
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        reqBody=request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie: cookies){
                if (cookie.getName().equals("firstname_workload")) firstname = cookie.getValue();
                if (cookie.getName().equals("lastname_workload")) lastname = cookie.getValue();
            }
        }
        Cookie remove_firstname = new Cookie("firstname","");
        Cookie remove_lastname = new Cookie("lastname","");
        remove_firstname.setMaxAge(0); response.addCookie(remove_firstname);
        remove_lastname.setMaxAge(0); response.addCookie(remove_lastname);
        response.sendRedirect("workload_page");
    }
}
