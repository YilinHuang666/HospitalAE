import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = {"/welcome_page"}, loadOnStartup = 2)
public class welcome_page extends HttpServlet {
    private String firstname;
    private String lastname;
    private static String disable_ava_select="";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        //Welcome_page html code
        out.println("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <style>\n" +
                "        h1 {\n" +
                "            color: black;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .button {\n" +
                "            background-color: darkmagenta;\n" +
                "            border: 2px solid black ;\n" +
                "            color: #f1f1f1;\n" +
                "            padding: 20px 40px;\n" +
                "            text-align: center;\n" +
                "            display: inline-block;\n" +
                "            font-size: 24px;\n" +
                "            border-radius: 6px;\n" +
                "            -webkit-transition-duration: 0.5s;\n" +
                "            transition-duration: 0.5s;\n" +
                "            cursor:pointer;\n" +
                "            width: 20%;\n" +
                "        }\n" +
                "\n" +
                "        .button:hover{\n" +
                "            background-color: white;\n" +
                "            color: black;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body >\n" +
                "    <h1>Welcome " + firstname + " "+ lastname+
                "</h1>\n" +
                "    &nbsp;\n" +
                "    <form action=\"timetable_page\" method=\"post\">\n" +
                "        <div style=\"text-align: center\" >\n" +
                "            <button class=\"button\" type=\"submit\">Timetable</button>\n" +
                "        </div>\n" +
                "    </form>\n" +
                "    &nbsp;\n" +
                "    <form action=\"mypatients_page\" method=\"post\">\n" +
                "        <div style=\"text-align: center\">\n" +
                "            <button class=\"button\" type=\"submit\">My Patients</button>\n" +
                "        </div>\n" +
                "    </form>\n" +
                "    &nbsp;\n" +
                "    <form action=\"workload_page\" method=\"post\">\n" +
                "        <div style=\"text-align: center\">\n" +
                "            <button class=\"button\" type=\"submit\">Work Load</button>\n" +
                "        </div>\n" +
                "    </form>\n" +
                "    &nbsp;\n" +
                "    <form action=\"availability_selection_page\" method=\"post\">\n" +
                "        <div style=\"text-align: center\">\n" +
                "            <button class=\"button\" type=\"submit\" "+" "+ disable_ava_select +">Time Availability Selection</button>\n" +
                "        </div>\n" +
                "    </form>\n" +
                "    &nbsp;\n"+
                "   <form action=\"checkout_patient\" method=\"post\">" +
                "       <div style='text-align: center'>" +
                "           <button class='button' type='submit'>Discharge Patient</button>" +
                "       </div>" +
                "   </form>"+
                "   &nbsp;\n" +
                "   <form action='login' method='post'>" +
                "       <div style='text-align: center'>" +
                "           <button class='button' type='submit'>Logout</button>" +
                "       </div>" +
                "   </form>"+
                "</body>\n" +
                "</html>");
        Cookie firstname_cookie = new Cookie("firstname_select",firstname); //create cookies to convey firstname and lastname to other pages for identifying
        Cookie lastname_cookie = new Cookie("lastname_select",lastname);
        Cookie firstname_cookie_workload = new Cookie("firstname_workload",firstname);
        Cookie lastname_cookie_workload = new Cookie("lastname_workload",lastname);
        Cookie firstname_cookie_timetable = new Cookie("firstname_timetable",firstname);
        Cookie lastname_cookie_timetable = new Cookie("lastname_timetable",lastname);
        Cookie firstname_cookie_my_patient = new Cookie("firstname_my_patient",firstname);
        Cookie lastname_cookie_my_patient = new Cookie("lastname_my_patient",lastname);
        Cookie firstname_cookie_checkout = new Cookie("firstname_checkout",firstname);
        Cookie lastname_cookie_checkout = new Cookie("lastname_checkout",lastname);
        response.addCookie(firstname_cookie); response.addCookie(lastname_cookie); response.addCookie(lastname_cookie_workload); response.addCookie(firstname_cookie_workload); response.addCookie(firstname_cookie_timetable); response.addCookie(lastname_cookie_timetable); response.addCookie(firstname_cookie_my_patient); response.addCookie(lastname_cookie_my_patient); response.addCookie(firstname_cookie_checkout); response.addCookie(lastname_cookie_checkout);
        firstname_cookie.setMaxAge(10*60); lastname_cookie.setMaxAge(10*60); lastname_cookie_workload.setMaxAge(10*60); firstname_cookie_workload.setMaxAge(10*60); firstname_cookie_timetable.setMaxAge(10*60); lastname_cookie_timetable.setMaxAge(10*60); firstname_cookie_my_patient.setMaxAge(10*60); lastname_cookie_my_patient.setMaxAge(10*60); firstname_cookie_checkout.setMaxAge(10*60); lastname_cookie_checkout.setMaxAge(10*60);
        firstname_cookie.setPath("/availability_selection_page"); lastname_cookie.setPath("/availability_selection_page");
        lastname_cookie_workload.setPath("/workload_page"); firstname_cookie_workload.setPath("/workload_page");
        firstname_cookie_timetable.setPath("/timetable_page"); lastname_cookie_timetable.setPath("/timetable_page");
        firstname_cookie_my_patient.setPath("/mypatients_page"); lastname_cookie_my_patient.setPath("/mypatients_page");
        firstname_cookie_checkout.setPath("/checkout_patient"); lastname_cookie_checkout.setPath("/checkout_patient");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String dbUrl =  System.getenv("JDBC_DATABASE_URL");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {}
        Connection conn= null;
        try {
            conn = DriverManager.getConnection(dbUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String uname = (String)request.getAttribute("username"); //get the input username
        try {
            Statement s=conn.createStatement();
            String sqlcom="select * from doctor_login_info where username='"+uname+"';";
             ResultSet resultset = s.executeQuery(sqlcom);
            while(resultset.next()) {
                firstname = resultset.getString("firstname");
                lastname = resultset.getString("lastname");
            }
            resultset.close();
            s.close();
            conn.close();
        }
        catch(Exception e){}
       response.sendRedirect("welcome_page"); //redirect to welcome_page url for further operation
    }
}
