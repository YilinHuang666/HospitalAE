import java.sql.*;

public class Verificate{
    private static String dbUrl =  System.getenv("JDBC_DATABASE_URL");

    public static boolean checkinfo(String passw) throws SQLException {
        boolean state=false;
        try {
            // Registers the driver
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {}
        Connection conn= DriverManager.getConnection(dbUrl);
        try {
            Statement s=conn.createStatement();
            PreparedStatement ps=conn.prepareStatement("select * from doctor_login_info where password=?");
            ps.setString(1,passw);
            ResultSet resultset = ps.executeQuery();
            state=resultset.next();
            resultset.close();
            s.close();
            conn.close();
        }
        catch (Exception e){
        }

        return state;
    }
}
