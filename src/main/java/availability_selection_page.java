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

@WebServlet(urlPatterns = {"/availability_selection_page"},loadOnStartup = 1)
public class availability_selection_page extends HttpServlet {
    private static String disable_1a, disable_2a, disable_3a, disable_4a, disable_5a, disable_6a, disable_7a,
            disable_1b, disable_2b, disable_3b, disable_4b, disable_5b, disable_6b, disable_7b,
            disable_1c, disable_2c, disable_3c, disable_4c, disable_5c, disable_6c, disable_7c ="";
    private static String lastname;
    private static String firstname;
    private static String time_slot[];
    private static String time_slot_message="";
    private static int chosen_checkbox_count=0;
    private static String dbUrl =  System.getenv("JDBC_DATABASE_URL");
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<style>\n" +
                "    /*reference 1: taken from https://www.w3schools.com/howto/howto_css_custom_checkbox.asp*/\n" +
                "    /* The container */\n" +
                "    .container {\n" +
                "        display: block;\n" +
                "        position: relative;\n" +
                "        padding-left: 35px;\n" +
                "        margin-bottom: 12px;\n" +
                "        cursor: pointer;\n" +
                "        font-size: 16px;\n" +
                "        -webkit-user-select: none;\n" +
                "        -moz-user-select: none;\n" +
                "        -ms-user-select: none;\n" +
                "        user-select: none;\n" +
                "        margin-left: 16cm;\n" +
                "    }\n" +
                "\n" +
                "    /* Hide the browser's default checkbox */\n" +
                "    .container input {\n" +
                "        position: absolute;\n" +
                "        opacity: 0;\n" +
                "        cursor: pointer;\n" +
                "        height: 0;\n" +
                "        width: 0;\n" +
                "    }\n" +
                "\n" +
                "    /* Create a custom checkbox */\n" +
                "    .checkmark {\n" +
                "        position: absolute;\n" +
                "        top: 0;\n" +
                "        left: 0;\n" +
                "        height: 25px;\n" +
                "        width: 25px;\n" +
                "        background-color: #eee;\n" +
                "    }\n" +
                "\n" +
                "    /* On mouse-over, add a grey background color */\n" +
                "    .container:hover input ~ .checkmark {\n" +
                "        background-color: #ccc;\n" +
                "    }\n" +
                "\n" +
                "    /* When the checkbox is checked, add a blue background */\n" +
                "    .container input:checked ~ .checkmark {\n" +
                "        background-color: #2196F3;\n" +
                "    }\n" +
                "\n" +
                "    /* Create the checkmark/indicator (hidden when not checked) */\n" +
                "    .checkmark:after {\n" +
                "        content: \"\";\n" +
                "        position: absolute;\n" +
                "        display: none;\n" +
                "    }\n" +
                "\n" +
                "    /* Show the checkmark when checked */\n" +
                "    .container input:checked ~ .checkmark:after {\n" +
                "        display: block;\n" +
                "    }\n" + "     /*when the checkbox is disabled, add red background*/\n" +
                "    .container input:disabled ~ .checkmark {\n" +
                "        background-color: firebrick;\n" +
                "        border-color: #f44336;\n" +
                "    }" +
                "    /* Style the checkmark/indicator */\n" +
                "    .container .checkmark:after {\n" +
                "        left: 9px;\n" +
                "        top: 5px;\n" +
                "        width: 5px;\n" +
                "        height: 10px;\n" +
                "        border: solid white;\n" +
                "        border-width: 0 3px 3px 0;\n" +
                "        -webkit-transform: rotate(45deg);\n" +
                "        -ms-transform: rotate(45deg);\n" +
                "        transform: rotate(45deg);\n" +
                "    }\n" +
                "\n" +
                "    .container input:disabled{\n" +
                "        color: #e2e2e2;\n" +
                "    }\n" +
                "\n" +
                "    .button {\n" +
                "        background-color: darkmagenta;\n" +
                "        border: 2px solid black ;\n" +
                "        color: #f1f1f1;\n" +
                "        padding: 20px 40px;\n" +
                "        text-align: center;\n" +
                "        display: inline-block;\n" +
                "        font-size: 24px;\n" +
                "        border-radius: 6px;\n" +
                "        -webkit-transition-duration: 0.5s;\n" +
                "        transition-duration: 0.5s;\n" +
                "        cursor:pointer;\n" +
                "        width: 20%;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    .button:hover{\n" +
                "        background-color: white;\n" +
                "        color: black;\n" +
                "    }\n" +
                "    /*end of reference 1*/\n" +
                "    h1{\n" +
                "        text-align: center;\n" +
                "    }\n" +
                "</style>\n" +
                "<body>\n" +
                "    <h1>Time Availability Selection</h1>\n" +
                "    <form action=\"availability_selection_page\" method=\"post\">\n" +
                "    <label class=\"container\">Monday 12:00am-8:00am\n" +
                "        <input type=\"checkbox\" name=\"time_slot\" value=\"1a\"" + " " + disable_1a +
                ">\n" +
                "        <span class=\"checkmark\"></span>\n" +
                "    </label>\n" +
                "    <label class=\"container\">Monday 8:00am-4:00pm\n" +
                "        <input type=\"checkbox\" name=\"time_slot\" value=\"1b\"" + " " + disable_1b +
                ">\n" +
                "        <span class=\"checkmark\"></span>\n" +
                "    </label>\n" +
                "    <label class=\"container\">Monday 4:00pm-12:00am\n" +
                "        <input type=\"checkbox\" name=\"time_slot\" value=\"1c\"" + " " + disable_1c +
                ">\n" +
                "        <span class=\"checkmark\"></span>\n" +
                "    </label>\n" +
                "    <label class=\"container\">Tuesday 12:00am-8:00am\n" +
                "        <input type=\"checkbox\" name=\"time_slot\" value=\"2a\"" + " " + disable_2a +
                ">\n" +
                "        <span class=\"checkmark\"></span>\n" +
                "    </label>\n" +
                "    <label class=\"container\">Tuesday 8:00am-4:00pm\n" +
                "        <input type=\"checkbox\" name=\"time_slot\" value=\"2b\"" + " " + disable_2b +
                ">\n" +
                "        <span class=\"checkmark\"></span>\n" +
                "    </label>\n" +
                "    <label class=\"container\">Tuesday 4:00pm-12:00am\n" +
                "        <input type=\"checkbox\" name=\"time_slot\" value=\"2c\"" + " " + disable_2c +
                ">\n" +
                "        <span class=\"checkmark\"></span>\n" +
                "    </label>\n" +
                "    <label class=\"container\">Wednesday 12:00am-8:00am\n" +
                "        <input type=\"checkbox\" name=\"time_slot\" value=\"3a\"" + " " + disable_3a +
                ">\n" +
                "        <span class=\"checkmark\"></span>\n" +
                "    </label>\n" +
                "    <label class=\"container\">Wednesday 8:00am-4:00pm\n" +
                "        <input type=\"checkbox\" name=\"time_slot\" value=\"3b\"" + " " + disable_3b +
                ">\n" +
                "        <span class=\"checkmark\"></span>\n" +
                "    </label>\n" +
                "    <label class=\"container\">Wednesday 4:00pm-12:00am\n" +
                "        <input type=\"checkbox\" name=\"time_slot\" value=\"3c\"" + " " + disable_3c +
                ">\n" +
                "        <span class=\"checkmark\"></span>\n" +
                "    </label>\n" +
                "    <label class=\"container\">Thursday 12:00am-8:00am\n" +
                "        <input type=\"checkbox\" name=\"time_slot\" value=\"4a\"" + " " + disable_4a +
                ">\n" +
                "        <span class=\"checkmark\"></span>\n" +
                "    </label>\n" +
                "    <label class=\"container\">Thursday 8:00am-4:00pm\n" +
                "        <input type=\"checkbox\" name=\"time_slot\" value=\"4b\"" + " " + disable_4b +
                ">\n" +
                "        <span class=\"checkmark\"></span>\n" +
                "    </label>\n" +
                "    <label class=\"container\">Thursday 4:00pm-12:00am\n" +
                "        <input type=\"checkbox\" name=\"time_slot\" value=\"4c\"" + " " + disable_4c +
                ">\n" +
                "        <span class=\"checkmark\"></span>\n" +
                "    </label>\n" +
                "    <label class=\"container\">Friday 12:00am-8:00am\n" +
                "        <input type=\"checkbox\" name=\"time_slot\" value=\"5a\"" + " " + disable_5a +
                ">\n" +
                "        <span class=\"checkmark\"></span>\n" +
                "    </label>\n" +
                "    <label class=\"container\">Friday 8:00am-4:00pm\n" +
                "        <input type=\"checkbox\" name=\"time_slot\" value=\"5b\"" + " " + disable_5b +
                ">\n" +
                "        <span class=\"checkmark\"></span>\n" +
                "    </label>\n" +
                "    <label class=\"container\">Friday 4:00pm-12:00am\n" +
                "        <input type=\"checkbox\" name=\"time_slot\" value=\"5c\"" + " " + disable_5c +
                ">\n" +
                "        <span class=\"checkmark\"></span>\n" +
                "    </label>\n" +
                "    <label class=\"container\">Saturday 12:00am-8:00am\n" +
                "        <input type=\"checkbox\" name=\"time_slot\" value=\"6a\"" + " " + disable_6a +
                ">\n" +
                "        <span class=\"checkmark\"></span>\n" +
                "    </label>\n" +
                "    <label class=\"container\">Saturday 8:00am-4:00pm\n" +
                "        <input type=\"checkbox\" name=\"time_slot\" value=\"6b\"" + " " + disable_6b +
                ">\n" +
                "        <span class=\"checkmark\"></span>\n" +
                "    </label>\n" +
                "    <label class=\"container\">Saturday 4:00pm-12:00am\n" +
                "        <input type=\"checkbox\" name=\"time_slot\" value=\"6c\"" + " " + disable_6c +
                ">\n" +
                "        <span class=\"checkmark\"></span>\n" +
                "    </label>\n" +
                "    <label class=\"container\">Sunday 12:00am-8:00am\n" +
                "        <input type=\"checkbox\" name=\"time_slot\" value=\"7a\"" + " " + disable_7a +
                ">\n" +
                "        <span class=\"checkmark\"></span>\n" +
                "    </label>\n" +
                "    <label class=\"container\">Sunday 8:00am-4:00pm\n" +
                "        <input type=\"checkbox\" name=\"time_slot\" value=\"7b\"" + " " + disable_7b +
                ">\n" +
                "        <span class=\"checkmark\"></span>\n" +
                "    </label>\n" +
                "    <label class=\"container\">Sunday 4:00pm-12:00am\n" +
                "        <input type=\"checkbox\" name=\"time_slot\" value=\"7c\"" + " " + disable_7c +
                ">\n" +
                "        <span class=\"checkmark\"></span>\n" +
                "    </label>\n" +
                "    &nbsp;\n" +
                "        <div style=\"text-align: center\">\n" +
                "            <button class=\"button\" type=\"submit\">Submit</button>\n" +
                "        </div>\n" +
                "   </form>\n" +
                "<form action='login' method='post'> " +
                "   <div style='text-align: center'>" +
                "       <button class='button' type='submit'>Logout</button>" +
                "   </div>" +
                "</form>" +
                "</body>\n" +
                "</html>");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
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
        response.addCookie(firstname_remove); response.addCookie(lastname_remove); //remove cookie
        time_slot=request.getParameterValues("time_slot"); //obtain input timetable

        if (time_slot!=null){
            for (int i=0; i<time_slot.length; i++){
                time_slot_message+=time_slot[i]+" ";
                switch (time_slot[i]){ //disable chosen checkbox
                    case "1a": disable_1a="disabled"; chosen_checkbox_count++; break;
                    case "2a": disable_2a="disabled"; chosen_checkbox_count++; break;
                    case "3a": disable_3a="disabled"; chosen_checkbox_count++; break;
                    case "4a": disable_4a="disabled"; chosen_checkbox_count++; break;
                    case "5a": disable_5a="disabled"; chosen_checkbox_count++; break;
                    case "6a": disable_6a="disabled"; chosen_checkbox_count++; break;
                    case "7a": disable_7a="disabled"; chosen_checkbox_count++; break;
                    case "1b": disable_1b="disabled"; chosen_checkbox_count++; break;
                    case "2b": disable_2b="disabled"; chosen_checkbox_count++; break;
                    case "3b": disable_3b="disabled"; chosen_checkbox_count++; break;
                    case "4b": disable_4b="disabled"; chosen_checkbox_count++; break;
                    case "5b": disable_5b="disabled"; chosen_checkbox_count++; break;
                    case "6b": disable_6b="disabled"; chosen_checkbox_count++; break;
                    case "7b": disable_7b="disabled"; chosen_checkbox_count++; break;
                    case "1c": disable_1c="disabled"; chosen_checkbox_count++; break;
                    case "2c": disable_2c="disabled"; chosen_checkbox_count++; break;
                    case "3c": disable_3c="disabled"; chosen_checkbox_count++; break;
                    case "4c": disable_4c="disabled"; chosen_checkbox_count++; break;
                    case "5c": disable_5c="disabled"; chosen_checkbox_count++; break;
                    case "6c": disable_6c="disabled"; chosen_checkbox_count++; break;
                    case "7c": disable_7c="disabled"; chosen_checkbox_count++; break;
                    default: break;
                }
            }
            try {
                // Registers the driver
                Class.forName("org.postgresql.Driver");
            } catch (Exception e) {}
            try {
                Connection conn= DriverManager.getConnection(dbUrl);  //connect to database
                Statement s=conn.createStatement();
                PreparedStatement ps=conn.prepareStatement("update doctor_login_info set timetable=? where lastname=?"); // to check if the database has the corresponding information
                ps.setString(1,time_slot_message); ps.setString(2,lastname);
                ResultSet resultset = ps.executeQuery();
                resultset.close();
                s.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (chosen_checkbox_count==21){ //if all time slots are chosen, reset the whole page
            disable_1a=disable_2a=disable_3a=disable_4a=disable_5a=disable_6a=disable_7a=
            disable_1b=disable_2b=disable_3b=disable_4b=disable_5b=disable_6b=disable_7b=
            disable_1c=disable_2c=disable_3c=disable_4c=disable_5c=disable_6c=disable_7c ="";
            chosen_checkbox_count=0;
        }
        time_slot_message = "";
        response.sendRedirect("availability_selection_page");
    }
}
