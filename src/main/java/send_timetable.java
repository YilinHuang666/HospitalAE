import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/send_timetable",loadOnStartup = 1)
public class send_timetable extends HttpServlet {
    private final static String dbUrl=System.getenv("JDBC_DATABASE_URL");
    private ArrayList<String> all_firstname=new ArrayList<String>();
    private ArrayList<String> all_lastname=new ArrayList<String>();
    private ArrayList<String> all_timetable=new ArrayList<String>();
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
        try {
            conn= DriverManager.getConnection(dbUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{//select all doctor's first name then store them in the arraylist
            PreparedStatement ps = conn.prepareStatement("SELECT firstname from doctor_login_info");
            ResultSet resultSet=ps.executeQuery();
            while (resultSet.next()){
                all_firstname.add(resultSet.getString("firstname"));
            }
        } catch (Exception e){}
        try{//select all doctor's last name then store them in the arraylist
            PreparedStatement ps = conn.prepareStatement("SELECT lastname from doctor_login_info");
            ResultSet resultSet=ps.executeQuery();
            while (resultSet.next()){
                all_lastname.add(resultSet.getString("lastname"));
            }
        }catch (Exception e){}
        try{//select all doctor's timetable then store them in the arraylist
            PreparedStatement ps = conn.prepareStatement("SELECT timetable from doctor_login_info");
            ResultSet resultSet=ps.executeQuery();
            while (resultSet.next()){
                all_timetable.add(resultSet.getString("timetable"));
            }
        }catch (Exception e){}
        for (int i=0; i<all_timetable.size(); i++){
            out.println(all_firstname.get(i)+" "+all_lastname.get(i));
            out.println(all_timetable.get(i));
        }
        all_firstname.removeAll(all_firstname);
        all_lastname.removeAll(all_lastname);
        all_timetable.removeAll(all_timetable);
    }
}
