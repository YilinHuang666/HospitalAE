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
    private String dbUrl =  System.getenv("JDBC_DATABASE_URL");
    private ArrayList<String> c_r_p_fn = new ArrayList<String>(); //list of current responsible patient firstname
    private ArrayList<String> c_r_p_ln = new ArrayList<String>(); //list of current responsible patient lastname
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<body>\n" +
                "<h2>Check out Patient</h2>\n" +
                "<form action=\"checkout_patient\" method='post'>\n" +
                "    <p><h3>Please select the patient to discharge:</h3></p>\n");
        for (int i=0; i<c_r_p_fn.size(); i++){ //obtain all current responsible patients
            out.println("<input type=\"radio\" name=\"patient_name\" value=\""+c_r_p_fn.get(i)+"\">"+c_r_p_fn.get(i)+" "+c_r_p_ln.get(i)+"<br>\n");
        }
        out.println("<button type=\"submit\"> discharge </button>");
        out.println("\n" +
                "</form>\n" +
                "</body>\n" +
                "</html>");
        c_r_p_ln.clear(); c_r_p_fn.clear(); //clear out the array for printing next time

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

        Gson gson=new Gson();
        checkout_p_fn = request.getParameter("patient_name"); //get the patient's first name for checkout process
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
        if (checkout_p_fn!=null) {
            for (int i = 0; i < c_r_p_fn.size(); i++) {
                if (checkout_p_fn.equals(c_r_p_fn.get(i))) {
                    checkout_p_ln=c_r_p_ln.get(i);
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
        }
        response.sendRedirect("checkout_patient");
    }
}