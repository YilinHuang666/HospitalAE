import com.google.gson.Gson;
import Class.*;
import jdk.nashorn.internal.ir.debug.PrintVisitor;

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

@WebServlet(urlPatterns = "/checkout_patient", loadOnStartup = 1)
public class checkout_patient extends HttpServlet {
    private String d_firstname,d_lastname;
    private String checkout_p_fn,checkout_p_ln;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>\n" +
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
                "<h2>Discharge Patient</h2>\n" +
                "\n" +
                "<form action=\"checkout_patient\" method=\"post\">\n" +
                "\n" +
                "    <div class=\"container\">\n" +
                "        <b>Patient's Firstname</b>\n" +
                "        <input type=\"text\" placeholder=\"Enter patient's first name to checkout\" name=\"firstname\" required>\n" +
                "\n" +
                "        <b>Patient's Lastname</b>\n" +
                "        <input type=\"text\" placeholder=\"Enter patient's last name to checkout\" name=\"lastname\" required>\n" +
                "\n" +
                "        <button type=\"submit\"><h2>Discharge</h2></button>\n" +
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
                "<form action='welcome_page' method='post'>" +
                "   <button type='submit'><h2>Return to Welcome Page</h2></button>" +
                "</form>"+
                "\n" +
                "</body>\n" +
                "</html>\n");
        ArrayList<String> c_r_p_fn = new ArrayList<String>(); //list of current responsible patient firstname
        ArrayList<String> c_r_p_ln = new ArrayList<String>(); //list of current responsible patient lastname
        Gson gson=new Gson();
        String dbUrl =  System.getenv("JDBC_DATABASE_URL");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {}
        try {
            Connection conn= DriverManager.getConnection(dbUrl);
            Statement s=conn.createStatement();
            String sqlcom="SELECT * from patient_to_doctor_table where r_dr_firstname='"+d_firstname+"' and r_dr_lastname='"+d_lastname+"';"; // select the all current responsible patients of the doctor
            ResultSet resultSet=s.executeQuery(sqlcom);
            while (resultSet.next()){
                c_r_p_fn.add(resultSet.getString("patient_firstname"));
                c_r_p_ln.add(resultSet.getString("patient_lastname"));
            }
            conn.close();
            s.close();
            resultSet.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        //if (checkout_p_fn!=null && checkout_p_ln!=null) {
            for (int i = 0; i < c_r_p_fn.size(); i++) {
                if (checkout_p_fn.equals(c_r_p_fn.get(i)) && checkout_p_ln.equals(c_r_p_ln.get(i))) {
                    patient_to_doctor pd = new patient_to_doctor(checkout_p_fn, checkout_p_ln, d_firstname, d_lastname);
                    response.getWriter().write(gson.toJson(pd));
                    try {
                        Connection conn = DriverManager.getConnection(dbUrl);
                        Statement s = conn.createStatement();
                        String sqlcom = "delete from patient_to_doctor_table where patient_firstname='" + checkout_p_fn + "' and patient_lastname='" + checkout_p_ln + "';"; //delete the specific patient from the database to discharge the patient
                        s.execute(sqlcom);
                        conn.close();
                        s.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        //}
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        Cookie[] cookies = request.getCookies(); //receive the login doctor name
        if (cookies != null){
            for (Cookie cookie: cookies){
                if (cookie.getName().equals("firstname")) d_firstname = cookie.getValue();
                if (cookie.getName().equals("lastname")) d_lastname = cookie.getValue();
            }
        }
        Cookie remove_firstname = new Cookie("firstname",""); //remove cookie
        Cookie remove_lastname = new Cookie("lastname","");
        remove_firstname.setMaxAge(0); response.addCookie(remove_firstname);
        remove_lastname.setMaxAge(0); response.addCookie(remove_lastname);

        checkout_p_fn = request.getParameter("firstname"); //get the patient's first name for checkout process
        checkout_p_ln = request.getParameter("lastname"); // get the patient's last name for checkout process

        response.sendRedirect("checkout_patient");
    }
}