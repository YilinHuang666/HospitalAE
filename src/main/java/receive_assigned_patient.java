import com.google.gson.Gson;
import Class.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

//this url is not for the doctors to view, it is for receiving the assigned patient from the reception then add the info to the database
@WebServlet(urlPatterns = "/receive_assigned_patient",loadOnStartup = 1)
public class receive_assigned_patient extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String reqBody=request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        response.setContentType("text/html");
        if (reqBody!=null) {
            Gson gson = new Gson();
            patient_to_doctor pd = gson.fromJson(reqBody, patient_to_doctor.class); //receive assigned patient name and responsible doctor's name
            String patient_firstname = pd.getPatient_firstname();
            String patient_lastname = pd.getPatient_lastname();
            String r_doctor_firstname = pd.getResponsible_doctor_firstname();
            String r_doctor_lastname = pd.getResponsible_doctor_lastname();
            String dbUrl = System.getenv("JDBC_DATABASE_URL"); //add these information to database
            try {
                // Registers the driver
                Class.forName("org.postgresql.Driver");
            } catch (Exception e) {
            }
            try {
                Connection conn = DriverManager.getConnection(dbUrl);
                Statement s = conn.createStatement();
                String sqlcom = "insert into patient_to_doctor_table values ('" + patient_firstname + "','" + patient_lastname + "','" + //add information to database
                        r_doctor_firstname + "','" + r_doctor_lastname + "');";
                s.execute(sqlcom);
                conn.close();
                s.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
