import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
//this url is not for the doctor to view, it is used to send all doctors' timetable to the local reception
@WebServlet(urlPatterns = "/send_timetable",loadOnStartup = 1)
public class send_timetable extends HttpServlet {
    private final static String dbUrl=System.getenv("JDBC_DATABASE_URL");
    private ArrayList<String> all_firstname=new ArrayList<String>(); //these two arraylists are used to store all doctor's
    private ArrayList<String> all_lastname=new ArrayList<String>();  //first name and last name
    private ArrayList<String> all_timetable=new ArrayList<String>(); //arraylist to store all timetable info for all doctors
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        PrintWriter out=response.getWriter();
        try {
            // Registers the driver
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {}
        Connection conn=null;
        try{//select all doctor's first name then store them in the arraylist
            conn= DriverManager.getConnection(dbUrl);
            PreparedStatement ps = conn.prepareStatement("SELECT firstname from doctor_login_info");
            ResultSet resultSet=ps.executeQuery();
            while (resultSet.next()){
                all_firstname.add(resultSet.getString("firstname"));
            }
            conn.close();
            resultSet.close();
            ps.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        try{//select all doctor's last name then store them in the arraylist
            conn= DriverManager.getConnection(dbUrl);
            PreparedStatement ps = conn.prepareStatement("SELECT lastname from doctor_login_info");
            ResultSet resultSet=ps.executeQuery();
            while (resultSet.next()){
                all_lastname.add(resultSet.getString("lastname"));
            }
            conn.close();
            resultSet.close();
            ps.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{//select all doctor's timetable then store them in the arraylist
            conn= DriverManager.getConnection(dbUrl);
            PreparedStatement ps = conn.prepareStatement("SELECT timetable from doctor_login_info");
            ResultSet resultSet=ps.executeQuery();
            while (resultSet.next()){
                all_timetable.add(resultSet.getString("timetable"));
            }
            conn.close();
            resultSet.close();
            ps.close();
        }catch (Exception e){
            e.printStackTrace();
        }


        for (int i=0; i<all_timetable.size(); i++){
            out.println(all_firstname.get(i)+" "+all_lastname.get(i)); //print the timetable info so that local reception can receive these info
            out.println(all_timetable.get(i));
        }
        all_firstname.removeAll(all_firstname); //empty the arraylist to prevent repeated info
        all_lastname.removeAll(all_lastname);
        all_timetable.removeAll(all_timetable);
    }
}
