

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

//Doctors are able to view their timetable at this page. The timetable is formatted into a calendar for clearer
//visualization.
@WebServlet(urlPatterns = "/timetable_page", loadOnStartup = 1)

public class timetable_page extends HttpServlet {
    private String firstname;
    private String lastname;
    private final static String dbUrl = System.getenv("JDBC_DATABASE_URL");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
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
        String rawTimetable = "";
        try{
            Statement s = conn.createStatement();
            String sqlcom="SELECT * from doctor_login_info where firstname='"+firstname+"' and lastname='"+lastname+"';";
             ResultSet resultset = s.executeQuery(sqlcom);      //query database for doctors timetable
            while (resultset.next()){
                rawTimetable = resultset.getString("timetable");
            }
            String[] arrOfTb = rawTimetable.split(" ");
            sort(arrOfTb);
            out.println("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title></title>\n" +
                    "    <style>\n" +
                    "        table {\n" +
                    "            width: 1000px;\n" +                        //set table format
                    "            margin: auto;\n" +
                    "            text-align: center;\n" +
                    "            border: 1px solid purple;\n" +
                    "            border-bottom: 3px double purple;\n" +
                    "\n" +
                    "        }\n" +
                    "\n" +
                    "        th,\n" +
                    "        td {\n" +
                    "            border: 1px solid purple;\n" +
                    "        }\n" +
                    "\n" +
                    "        .today {\n" +
                    "            color: red;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "    <script>\n" +
                    "        function checkslot(ind, knd, array) {\n" +
                    "            var a;\n" +
                    "            for (a = 0;a < array.length;a++){\n" +     //function for check which
                    "                if (array[a] == 10*knd +ind){\n" +     //time the doctor is on duty
                    "                    return true;\n" +
                    "                }\n" +
                    "            }\n" +
                    "\n" +
                    "            return false;\n" +
                    "        }\n" +
                    "\n" +
                    "        var i, k;\n" +
                    "            today = new Date(); //get today's date\n" +
                    "            arrayOfSlot = new Array(");
            for (int k = 0;k< arrOfTb.length;k++){
                if (k != 0){
                    out.println(",");
                }
                out.println(arrOfTb[k]);            //put values of timetable in arrayOfSlot
            }
            out.println(");\n" +
                    "            dayOfWeek = today.getDay(); //get the week day of today\n" +
                    "            if (dayOfWeek == 0)\n" +
                    "                dayOfWeek +=6;\n" +
                    "\n" +
                    "        document.write(\"<table cellspacing='0' cellpadding='10'>\" +\n" +
                    "            \"<tr><td width='80'></td>\" +\n" +                    //table head lines
                    "            \"<td width='130'>Monday</td>\" +\n" +
                    "            \"<td width='130'>Tuesday </td>\" +\n" +
                    "            \"<td width='130'>Wednesday</td>\" +\n" +
                    "            \"<td width='130'>Thursday</td>\" +\n" +
                    "            \"<td width='130'>Friday </td>\" +\n" +
                    "            \"<td width='130'>Saturday</td>\" +\n" +
                    "            \"<td width='130'>Sunday </td>\" +\n" +
                    "            \"</tr>\"); //Print first line that shows weekdays\n" +
                    "\n" +
                    "        for(i = 0; i < 3; i++){\n" +
                    "            document.write('<tr>');\n" +
                    "\n" +
                    "            if (i == 0) {\n" +                                      //table side lines
                    "                document.write(\"<td>0:00 to 8:00</td>\");\n" +
                    "            } else if (i == 1) {\n" +
                    "                document.write(\"<td>8:00 to 16:00</td>\");\n" +
                    "            } else if (i == 2) {\n" +
                    "                document.write(\"<td>16:00 to 24:00</td>\");\n" +
                    "            }\n" +
                    "            for (k = 0; k < 7; k ++) {\n" +
                    "                if(checkslot(i+1,k+1,arrayOfSlot)){\n" +           //check array to put on duty
                    "                    document.write(\"<td>On Duty</td>\");\n" +     //in correct slot
                    "                }else {\n" +
                    "                    document.write(\"<td></td>\");\n" +
                    "                }\n" +
                    "            }\n" +
                    "            document.write('</tr>');\n" +
                    "        }\n" +
                    "        document.write('</table>');\n" +
                    "    </script>\n" +
                    "</head>\n" +
                    "\n" +
                    "<body>\n" +
                    "</body>\n" +
                    "</html>");
            s.close();
            resultset.close();
            conn.close();

        }catch(Exception e){}


    }

    private void sort(String[] arrOfTb) {
        for(int k =0;k<arrOfTb.length;k++){
            char ka = arrOfTb[k].charAt(0);
            if (arrOfTb[k].charAt(1)=='a') {
                arrOfTb[k] = String.valueOf(ka) + "1";
            } else if(arrOfTb[k].charAt(1)=='b'){
                arrOfTb[k] = String.valueOf(ka) + "2";
            } else if(arrOfTb[k].charAt(1)=='c'){           //Convert a b c to 1 2 3, which is used in html as time slots
                arrOfTb[k] = String.valueOf(ka) + "3";
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out=response.getWriter();
        Cookie[] cookies = request.getCookies(); //get login doctor name from welcome page
        if (cookies != null){
            for (Cookie cookie: cookies){
                if (cookie.getName().equals("firstname_timetable")) firstname = cookie.getValue();
                if (cookie.getName().equals("lastname_timetable")) lastname = cookie.getValue();
            }
        }
        Cookie firstname_remove = new Cookie("firstname_timetable",""); //remove cookie
        Cookie lastname_remove = new Cookie("lastname_timetable","");
        firstname_remove.setMaxAge(0); lastname_remove.setMaxAge(0);
        response.addCookie(firstname_remove); response.addCookie(lastname_remove);
        response.sendRedirect("timetable_page");
    }
}
