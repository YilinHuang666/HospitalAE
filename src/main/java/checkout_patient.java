import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.nimbus.State;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/checkout_patient", loadOnStartup = 1)
public class checkout_patient extends HttpServlet {
    private String d_firstname,d_lastname;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<body>\n" +
                "<h2>Check out Patient</h2>\n" +
                "<form action=\"checkout_patient\" method='post'>\n" +
                "    Patient's first name:<br>\n" +
                "    <input type=\"text\" name=\"firstname\" value=\"Enter patient's firstname \">\n" +
                "    <br><br>\n" +
                "    Patient's last name:<br>\n" +
                "    <input type=\"text\" name=\"lastname\" value=\"Enter patient's lastname\">\n" +
                "    <br><br>\n" +
                "    <input type=\"submit\" value=\"Check out\">\n" +
                "</form>\n" +
                "</body>\n" +
                "</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        ArrayList<String> c_r_p_fn = new ArrayList<String>(); //list of current responsible patient firstname
        ArrayList<String> c_r_p_ln = new ArrayList<String>(); //list of current responsible patient lastname
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
        Gson gson=new Gson();
        String checkout_p_fn = request.getParameter("firstname"); //get the patient's first name for checkout process
        String checkout_p_ln = request.getParameter("lastname"); // get the patient's last name for checkout process
        String dbUrl =  System.getenv("JDBC_DATABASE_URL");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {}
        try {
            Connection conn= DriverManager.getConnection(dbUrl);
            Statement s=conn.createStatement();
            String sqlcom="SELECT * from patient_to_doctor_table where r_dr_firstname='"+d_firstname+"' and r_dr_lastname='"+d_lastname+"';";
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
        for (int i=0; i<c_r_p_fn.size(); i++) {
            if (checkout_p_fn.equals(c_r_p_fn.get(i)) && checkout_p_ln.equals(c_r_p_ln.get(i))) {
                patient_to_doctor pd = new patient_to_doctor(checkout_p_fn, checkout_p_ln, d_firstname, d_lastname);
                response.getWriter().write(gson.toJson(pd));
                try{
                    Connection conn=DriverManager.getConnection(dbUrl);
                    Statement s=conn.createStatement();
                    String sqlcom="delete from patient_to_doctor_table where patient_firstname='"+checkout_p_fn+"' and patient_lastname='"+checkout_p_ln+"';";
                    s.execute(sqlcom);
                    conn.close();
                    s.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
        response.sendRedirect("checkout_patient");
    }
}