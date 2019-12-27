package Functions;

import java.sql.*;

public class Verificate{
    private static String dbUrl =  System.getenv("JDBC_DATABASE_URL");


    public static boolean checkinfo(String passw, String usern) throws SQLException {
        boolean state=false;
        try {
            // Registers the driver
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {}
        Connection conn= DriverManager.getConnection(dbUrl);
        try {
            Statement s=conn.createStatement();
            PreparedStatement ps=conn.prepareStatement("select * from doctor_login_info where password=? and username=?"); // to check if the database has the corresponding information
            ps.setString(1,passw); ps.setString(2,usern);
            ResultSet resultset = ps.executeQuery();
            state=resultset.next(); //state=true if there is corresponding info, else state=false
            resultset.close();
            s.close();
            conn.close();
        }
        catch (Exception e){
        }

        return state;
    }
}
