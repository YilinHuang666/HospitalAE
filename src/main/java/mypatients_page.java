import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

//Doctors are able to view their responsible patients at this page
@WebServlet(urlPatterns = "/mypatients_page", loadOnStartup = 1)

public class mypatients_page extends HttpServlet {
    private static String firstname,lastname; //login doctor's first name and last name
    private final static String dbUrl =  System.getenv("JDBC_DATABASE_URL");
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        ArrayList<String> patient_fn_set=new ArrayList<String>(); //arraylists to store the doctor's responsible patients' name
        ArrayList<String> patient_ln_set=new ArrayList<String>();
        response.setContentType("text/html");
        PrintWriter out=response.getWriter();
        out.println("<h2>My Patients</h2>");
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
            PreparedStatement ps=conn.prepareStatement("select * from patient_to_doctor_table where r_dr_firstname=? and r_dr_lastname=?");
            ps.setString(1,firstname); ps.setString(2,lastname);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()){
                patient_fn_set.add(resultSet.getString("patient_firstname")); //select the doctor's responsible patients and store their name into the arraylist
                patient_ln_set.add(resultSet.getString("patient_lastname"));
            }
            conn.close();
            resultSet.close();
            ps.close();
            //s.close();
            for (int i=0; i<patient_fn_set.size(); i++){
                out.println("<h2>"+patient_fn_set.get(i)+" "+patient_ln_set.get(i)+"</h2>"); //print out patients' name
            }
        }catch(Exception e){}

}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Cookie[] cookies = request.getCookies(); //receive the login doctor name
        if (cookies != null){
            for (Cookie cookie: cookies){
                if (cookie.getName().equals("firstname_my_patient")) firstname = cookie.getValue();
                if (cookie.getName().equals("lastname_my_patient")) lastname = cookie.getValue();
            }
        }
        Cookie remove_firstname = new Cookie("firstname_my_patient",""); //remove cookie
        Cookie remove_lastname = new Cookie("lastname_my_patient","");
        remove_firstname.setMaxAge(0); response.addCookie(remove_firstname);
        remove_lastname.setMaxAge(0); response.addCookie(remove_lastname);
        response.sendRedirect("mypatients_page");
    }
}
