import com.google.gson.Gson;
import Class.*;
import jdk.nashorn.internal.ir.debug.PrintVisitor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

//doctors are able to discharge patients at this page. All his/her responsible patients' names will be shown on this
//page after a radio button. The doctor just need to pick which patient to discharge then click discharge. There is
//also a return to welcome page button on this page
@WebServlet(urlPatterns = "/discharge_patient", loadOnStartup = 1)
public class discharge_patient extends HttpServlet {
    private static String d_firstname,d_lastname; //login doctor's first name and last name
    private static String discharge_p_fn,discharge_p_ln; // patient's first name and last name to discharge
    private static String dbUrl =  System.getenv("JDBC_DATABASE_URL");
    private static ArrayList<String> c_r_p_fn = new ArrayList<String>(); //list of current responsible patient firstname
    private static ArrayList<String> c_r_p_ln = new ArrayList<String>(); //list of current responsible patient lastname
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>\n" + //html code for the page
                "<html lang=\"en\">\n" +
                "<body>\n" +
                "<h2>Discharge Patient</h2>\n" +
                "<form action=\"discharge_patient\" method='post'>\n" +
                "    <p><h3>Please select the patient to discharge:</h3></p>\n");
        for (int i=0; i<c_r_p_fn.size(); i++){ //obtain all current responsible patients
            out.println("<input type=\"radio\" name=\"patient_name\" value=\""+c_r_p_fn.get(i)+"\">"+c_r_p_fn.get(i)+" "+c_r_p_ln.get(i)+"<br>\n"); //print out all current responsible patients
        }
        out.println("<button type=\"submit\"> discharge </button>");
        out.println("\n" +
                "</form>\n" +
                "<form action='welcome_page' method='post'>" +
                "   <button type='submit'>Return to welcome page</button>" +
                "</form>" +
                "</body>\n" +
                "</html>");
        if (discharge_p_fn!=null){
            out.println(discharge_p_fn+" "+discharge_p_ln+" has been discharged"); //print notification message after successful discharge
        }
        c_r_p_ln.clear(); c_r_p_fn.clear(); //clear out the array for printing next time to prevent duplicated printing
        discharge_p_fn=null;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        Cookie[] cookies = request.getCookies(); //receive the login doctor name
        if (cookies != null){
            for (Cookie cookie: cookies){
                if (cookie.getName().equals("firstname_checkout")) d_firstname = cookie.getValue();
                if (cookie.getName().equals("lastname_checkout")) d_lastname = cookie.getValue();
            }
        }
        Cookie remove_firstname = new Cookie("firstname_checkout",""); //remove cookie
        Cookie remove_lastname = new Cookie("lastname_checkout","");
        remove_firstname.setMaxAge(0); response.addCookie(remove_firstname);
        remove_lastname.setMaxAge(0); response.addCookie(remove_lastname);

        Gson gson=new Gson();
        discharge_p_fn = request.getParameter("patient_name"); //get the patient's first name for checkout process
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {}
        Connection conn=null;
        try{
            conn=DriverManager.getConnection(dbUrl);
        }catch (SQLException e){
            e.printStackTrace();
        }
        try {
            //Statement s=conn.createStatement();
            //String sqlcom="SELECT * from patient_to_doctor_table where r_dr_firstname='"+d_firstname+"' and r_dr_lastname='"+d_lastname+"';"; // select the all current responsible patients of the doctor
            //ResultSet resultSet=s.executeQuery(sqlcom);
            PreparedStatement ps = conn.prepareStatement("select * from patient_to_doctor_table where r_dr_firstname=? and r_dr_lastname=?");
            ps.setString(1,d_firstname); ps.setString(2,d_lastname);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()){
                c_r_p_fn.add(resultSet.getString("patient_firstname")); //get all the current responsible patients' name and store them into the arraylist
                c_r_p_ln.add(resultSet.getString("patient_lastname"));
            }
            if (discharge_p_fn!=null) { //if the doctor select patient(s) to discharge
            for (int i = 0; i < c_r_p_fn.size(); i++) {
                if (discharge_p_fn.equals(c_r_p_fn.get(i))) {
                    discharge_p_ln=c_r_p_ln.get(i);
                    patient_to_doctor pd = new patient_to_doctor(discharge_p_fn, discharge_p_ln, d_firstname, d_lastname);
                    response.getWriter().write(gson.toJson(pd)); //send discharged patient's name and the responsible doctor's name
                    Statement s = conn.createStatement();
                    String sqlcom = "delete from patient_to_doctor_table where patient_firstname='" + discharge_p_fn + "' and patient_lastname='" + discharge_p_ln + "';"; //delete the specific patient from the database to discharge the patient
                    s.execute(sqlcom);
                    s.close();
                    c_r_p_fn.remove(discharge_p_fn); c_r_p_ln.remove(discharge_p_ln); //delete the discharged patient's name from the arratlist so that it won't be printed out on the web page
                    }
                }
            }
            conn.close();
            ps.close();
            resultSet.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect("discharge_patient");
    }
}