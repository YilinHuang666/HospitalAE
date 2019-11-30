import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/availability_selection_page"},loadOnStartup = 1)
public class availability_selection_page extends HttpServlet {
    private static final String ta_page = "<!DOCTYPE html>\n" +
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
            "    }\n" +
            "\n" +
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
            "        <input type=\"checkbox\" name=\"time_slot\" value=\"1a\">\n" +
            "        <span class=\"checkmark\"></span>\n" +
            "    </label>\n" +
            "    <label class=\"container\">Monday 8:00am-4:00pm\n" +
            "        <input type=\"checkbox\" name=\"time_slot\" value=\"1b\">\n" +
            "        <span class=\"checkmark\"></span>\n" +
            "    </label>\n" +
            "    <label class=\"container\">Monday 4:00pm-12:00am\n" +
            "        <input type=\"checkbox\" name=\"time_slot\" value=\"1c\">\n" +
            "        <span class=\"checkmark\"></span>\n" +
            "    </label>\n" +
            "    <label class=\"container\">Tuesday 12:00am-8:00am\n" +
            "        <input type=\"checkbox\" name=\"time_slot\" value=\"2a\">\n" +
            "        <span class=\"checkmark\"></span>\n" +
            "    </label>\n" +
            "    <label class=\"container\">Tuesday 8:00am-4:00pm\n" +
            "        <input type=\"checkbox\" name=\"time_slot\" value=\"2b\">\n" +
            "        <span class=\"checkmark\"></span>\n" +
            "    </label>\n" +
            "    <label class=\"container\">Tuesday 4:00pm-12:00am\n" +
            "        <input type=\"checkbox\" name=\"time_slot\" value=\"2c\">\n" +
            "        <span class=\"checkmark\"></span>\n" +
            "    </label>\n" +
            "    <label class=\"container\">Wednesday 12:00am-8:00am\n" +
            "        <input type=\"checkbox\" name=\"time_slot\" value=\"3a\">\n" +
            "        <span class=\"checkmark\"></span>\n" +
            "    </label>\n" +
            "    <label class=\"container\">Wednesday 8:00am-4:00pm\n" +
            "        <input type=\"checkbox\" name=\"time_slot\" value=\"3b\">\n" +
            "        <span class=\"checkmark\"></span>\n" +
            "    </label>\n" +
            "    <label class=\"container\">Wednesday 4:00pm-12:00am\n" +
            "        <input type=\"checkbox\" name=\"time_slot\" value=\"3c\">\n" +
            "        <span class=\"checkmark\"></span>\n" +
            "    </label>\n" +
            "    <label class=\"container\">Thursday 12:00am-8:00am\n" +
            "        <input type=\"checkbox\" name=\"time_slot\" value=\"4a\">\n" +
            "        <span class=\"checkmark\"></span>\n" +
            "    </label>\n" +
            "    <label class=\"container\">Thursday 8:00am-4:00pm\n" +
            "        <input type=\"checkbox\" name=\"time_slot\" value=4b\">\n" +
            "        <span class=\"checkmark\"></span>\n" +
            "    </label>\n" +
            "    <label class=\"container\">Thursday 4:00pm-12:00am\n" +
            "        <input type=\"checkbox\" name=\"time_slot\" value=\"4c\">\n" +
            "        <span class=\"checkmark\"></span>\n" +
            "    </label>\n" +
            "    <label class=\"container\">Friday 12:00am-8:00am\n" +
            "        <input type=\"checkbox\" name=\"time_slot\" value=\"5a\">\n" +
            "        <span class=\"checkmark\"></span>\n" +
            "    </label>\n" +
            "    <label class=\"container\">Friday 8:00am-4:00pm\n" +
            "        <input type=\"checkbox\" name=\"time_slot\" value=5b\">\n" +
            "        <span class=\"checkmark\"></span>\n" +
            "    </label>\n" +
            "    <label class=\"container\">Friday 4:00pm-12:00am\n" +
            "        <input type=\"checkbox\" name=\"time_slot\" value=\"5c\">\n" +
            "        <span class=\"checkmark\"></span>\n" +
            "    </label>\n" +
            "    <label class=\"container\">Saturday 12:00am-8:00am\n" +
            "        <input type=\"checkbox\" name=\"time_slot\" value=\"6a\">\n" +
            "        <span class=\"checkmark\"></span>\n" +
            "    </label>\n" +
            "    <label class=\"container\">Saturday 8:00am-4:00pm\n" +
            "        <input type=\"checkbox\" name=\"time_slot\" value=6b\">\n" +
            "        <span class=\"checkmark\"></span>\n" +
            "    </label>\n" +
            "    <label class=\"container\">Saturday 4:00pm-12:00am\n" +
            "        <input type=\"checkbox\" name=\"time_slot\" value=\"6c\">\n" +
            "        <span class=\"checkmark\"></span>\n" +
            "    </label>\n" +
            "    <label class=\"container\">Sunday 12:00am-8:00am\n" +
            "        <input type=\"checkbox\" name=\"time_slot\" value=\"7a\">\n" +
            "        <span class=\"checkmark\"></span>\n" +
            "    </label>\n" +
            "    <label class=\"container\">Sunday 8:00am-4:00pm\n" +
            "        <input type=\"checkbox\" name=\"time_slot\" value=7b\">\n" +
            "        <span class=\"checkmark\"></span>\n" +
            "    </label>\n" +
            "    <label class=\"container\">Sunday 4:00pm-12:00am\n" +
            "        <input type=\"checkbox\" name=\"time_slot\" value=\"7c\">\n" +
            "        <span class=\"checkmark\"></span>\n" +
            "    </label>\n" +
            "    &nbsp;\n" +
            "        <div style=\"text-align: center\">\n" +
            "            <button class=\"button\" type=\"submit\">Submit</button>\n" +
            "        </div>\n" +
            "    </form>\n" +
            "</body>\n" +
            "</html>";
    private static String lastname;
    private static String firstname;
    private static String time_slot[];
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(ta_page);
        out.println("<h2>"+time_slot[0]+"</h2>");
        out.println("<h2>"+firstname+"</h2>");
        out.println("<h2>"+lastname+"</h2>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
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
        //time_slot=request.getParameterValues("time_slot");
        response.getWriter().write(lastname);
        response.getWriter().write(firstname);
        response.sendRedirect("availability_selection_page");
    }

}
