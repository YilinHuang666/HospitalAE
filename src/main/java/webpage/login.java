package webpage;

import Functions.Verificate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/login"}, loadOnStartup = 1)
public class login extends HttpServlet {
    //html code for webpage.login page
    final static String doctor_login = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
            "    <style>\n" +
            "        body {font-family: Arial, Helvetica, sans-serif;}\n" +
            "        form {border: 4px solid #f1f1f1;}\n" +
            "\n" +
            "        input[type=text], input[type=password] {\n" +
            "            width: 100%;\n" +
            "            padding: 15px 25px;\n" +
            "            margin: 8px 0;\n" +
            "            display: inline-block;\n" +
            "            border: 3px solid #ccc;\n" +
            "            box-sizing: border-box;\n" +
            "        }\n" +
            "\n" +
            "        button {\n" +
            "            background-color: cornflowerblue;\n" +
            "            color: white;\n" +
            "            padding: 14px 10px;\n" +
            "            margin: 8px 0;\n" +
            "            border: none;\n" +
            "            cursor: pointer;\n" +
            "            width: 100%;\n" +
            "        }\n" +
            "\n" +
            "        button:hover {\n" +
            "            opacity: 0.8;\n" +
            "        }\n" +
            "\n" +
            "        <!--.cancelbtn {\n" +
            "            width: auto;\n" +
            "            padding: 10px 18px;\n" +
            "            background-color: #f44336;\n" +
            "        }-->\n" +
            "\n" +
            "        .imgcontainer {\n" +
            "            text-align: center;\n" +
            "            margin: 24px 0 12px 0;\n" +
            "        }\n" +
            "\n" +
            "        img.avatar {\n" +
            "            width: 40%;\n" +
            "            border-radius: 50%;\n" +
            "        }\n" +
            "\n" +
            "        .container {\n" +
            "            padding: 16px;\n" +
            "        }\n" +
            "\n" +
            "        span.psw {\n" +
            "            float: right;\n" +
            "            padding-top: 16px;\n" +
            "        }\n" +
            "\n" +
            "        /* Change styles for span and cancel button on extra small screens */\n" +
            "        @media screen and (max-width: 300px) {\n" +
            "            span.psw {\n" +
            "                display: block;\n" +
            "                float: none;\n" +
            "            }\n" +
            "            .cancelbtn {\n" +
            "                width: 100%;\n" +
            "            }\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "<h3>Doctor Login</h3>\n" +
            "\n" +
            "<form action=\"login\" method=\"post\">\n" +
            "\n" +
            "    <div class=\"container\">\n" +
            "        <b>Username</b>\n" +
            "        <input type=\"text\" placeholder=\"Enter Username\" name=\"uname\" required>\n" +
            "\n" +
            "        <b>Password</b>\n" +
            "        <input type=\"password\" placeholder=\"Enter Password\" name=\"psw\" required>\n" +
            "\n" +
            "        <button type=\"submit\"><h2>Login</h2></button>\n" +
            "       <!-- <label>\n" +
            "            <input type=\"checkbox\" checked=\"checked\" name=\"remember\"> Remember me\n" +
            "        </label>-->\n" +
            "    </div>\n" +
            "\n" +
            "   <!-- <div class=\"container\" style=\"background-color:#f1f1f1\">\n" +
            "        <button type=\"button\" class=\"cancelbtn\">Cancel</button>\n" +
            "        <span class=\"psw\">Forgot <a href=\"#\">password?</a></span>\n" +
            "    </div>-->\n" +
            "</form>\n" +
            "\n" +
            "</body>\n" +
            "</html>\n";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(doctor_login);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("uname");    //get username input
        String password = request.getParameter("psw");      //get password input
        //request.getSession().setAttribute("username",username); //store the username to attribute then send it to webpage.welcome_page for display of doctor's name
        HttpSession session = request.getSession();
        session.setAttribute("username",username);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        try {
            if (Verificate.checkinfo(password,username)) {                   //if password is valid then go to webpage.welcome_page
                RequestDispatcher rd = request.getRequestDispatcher("webpage.welcome_page");
                rd.forward(request,response);
            }
            else {
                if (username!=null) {
                    out.println("<h2>Invalid username or password!</h2>");//if not, display error message
                }
                out.println(doctor_login);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
