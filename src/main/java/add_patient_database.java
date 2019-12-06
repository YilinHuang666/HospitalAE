import com.google.gson.Gson;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/add_patient_database"}, loadOnStartup = 1)
public class add_patient_database extends HttpServlet {
    private static String dbUrl =  System.getenv("JDBC_DATABASE_URL");
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String reqBody=request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Gson gson=new Gson();
        patients p = gson.fromJson(reqBody,patients.class);
        int id=p.getId(); String firstname=p.getFirstname(); String lastname=p.getLastname(); String phonenumber=p.getPhonenumber();
        String identitynumber=p.getIdentitynumber(); String age=p.getAge(); String notes=p.getNotes();
        boolean admit_status=p.getAdmit_status();
        try {
            // Registers the driver
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {}
        try{
            Connection conn= DriverManager.getConnection(dbUrl);
            PreparedStatement ps=conn.prepareStatement("INSERT into patients(id, givenname, familyname, phonenumber, identitynumber, age, notes, admit_status) values (?,?,?,?,?,?,?,?)"); //add patient to database
            ps.setInt(1,id); ps.setString(2,firstname); ps.setString(3,lastname); ps.setString(4, phonenumber); ps.setString(5,identitynumber); ps.setString(6,age); ps.setString(7,notes); ps.setBoolean(8,admit_status);
        } catch (SQLException e){
            e.printStackTrace();
        }

    }
}
