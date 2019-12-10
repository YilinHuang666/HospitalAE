import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
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
                "   <form action=\"checkout_patient\" method=\"post\">" +
                "       <div style='text-align: center'>" +
                "           <button class='button' type='submit'>Checkout Patient</button>" +
                "       </div>" +
                "   </form>"+
                "</body>\n" +
                "</html>");
        Cookie firstname_cookie = new Cookie("firstname",firstname); //create cookies to convey firstname and lastname to other pages for identifying
        Cookie lastname_cookie = new Cookie("lastname",lastname);
        Cookie firstname_cookie_workload = new Cookie("firstname_workload",firstname);
        Cookie lastname_cookie_workload = new Cookie("lastname_workload",lastname);
        Cookie firstname_cookie_timetable = new Cookie("firstname",firstname);
        Cookie lastname_cookie_timetable = new Cookie("lastname",lastname);
        response.addCookie(firstname_cookie); response.addCookie(lastname_cookie); response.addCookie(lastname_cookie_workload); response.addCookie(firstname_cookie_workload); response.addCookie(firstname_cookie_timetable); response.addCookie(lastname_cookie_timetable);
        firstname_cookie.setMaxAge(10*60); lastname_cookie.setMaxAge(10*60); lastname_cookie_workload.setMaxAge(10*60); firstname_cookie_workload.setMaxAge(10*60); firstname_cookie_timetable.setMaxAge(10*60); lastname_cookie_timetable.setMaxAge(10*60);
        firstname_cookie.setPath("/availability_selection_page"); lastname_cookie.setPath("/availability_selection_page");
        lastname_cookie_workload.setPath("/workload_page"); firstname_cookie_workload.setPath("/workload_page");
        firstname_cookie_timetable.setPath("/timetable_page"); lastname_cookie_timetable.setPath("/timetable_page");
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
            PreparedStatement ps=conn.prepareStatement("select * from doctor_login_info where username=?"); //find the corresponding name to the username then display welcome message
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
        //Cookie[] cookies = request.getCookies(); //get login doctor name from welcome page
        //if (cookies != null){
        //    for (Cookie cookie: cookies){
        //        if (cookie.getName().equals("chosen_check_count")) disable_ava_select = cookie.getValue(); // if all time slots are chosen, disable the button to prevent further access
        //    }
       // }
        //if (disable_ava_select.equals("21")) disable_ava_select="disabled";
       // else disable_ava_select="";
       response.sendRedirect("welcome_page"); //redirect to welcome_page url for further operation
    }
}
