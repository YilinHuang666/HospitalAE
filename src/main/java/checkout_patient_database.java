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

@WebServlet(urlPatterns = {"/checkout_patient_database"}, loadOnStartup = 1)
public class checkout_patient_database extends HttpServlet {
    private static String dbUrl =  System.getenv("JDBC_DATABASE_URL");
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String reqBody=request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Gson gson=new Gson();
        patients p = gson.fromJson(reqBody,patients.class);
        String firstname=p.getFirstname(); String lastname=p.getLastname();
        String identitynumber=p.getIdentitynumber();
        boolean admit_status=p.getAdmit_status();
        try {
            // Registers the driver
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {}
        try{
            Connection conn= DriverManager.getConnection(dbUrl);
            PreparedStatement ps=conn.prepareStatement("delete from patients when givenname=? and familyname=? and identitynumber=?");//delete patient data from database
            ps.setString(1,firstname); ps.setString(2,lastname); ps.setString(3,identitynumber);
        } catch (SQLException e){
            e.printStackTrace();
        }

    }
}
