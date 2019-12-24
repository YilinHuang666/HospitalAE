package webpage;

import Functions.TBtowrite;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = "/webpage.timetable_page", loadOnStartup = 1)

public class timetable_page extends HttpServlet {
    private static String reqBody;
    private String firstname;
    private String lastname;
    private final static String dbUrl = System.getenv("JDBC_DATABASE_URL");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        PrintWriter out=response.getWriter();
        //out.println("<h2>Timetable</h2>");

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
             ResultSet resultset = s.executeQuery(sqlcom);
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
                    "            width: 1000px;\n" +
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
                    "            for (a = 0;a < array.length;a++){\n" +
                    "                if (array[a] == 10*knd +ind){\n" +
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
                out.println(arrOfTb[k]);    //put values of timetable in
            }
            out.println(");\n" +
                    "            dayOfWeek = today.getDay(); //get the week day of today\n" +
                    "            if (dayOfWeek == 0)\n" +
                    "                dayOfWeek +=6;\n" +
                    "\n" +
                    "        document.write(\"<table cellspacing='0' cellpadding='10'>\" +\n" +
                    "            \"<tr><td width='80'></td>\" +\n" +
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
                    "            if (i == 0) {\n" +
                    "                document.write(\"<td>0:00AM to 8:00AM</td>\");\n" +
                    "            } else if (i == 1) {\n" +
                    "                document.write(\"<td>8:00AM to 16:00PM</td>\");\n" +
                    "            } else if (i == 2) {\n" +
                    "                document.write(\"<td>16:00PM to 24:00PM</td>\");\n" +
                    "            }\n" +
                    "            for (k = 0; k < 7; k ++) {\n" +
                    "                if(checkslot(i+1,k+1,arrayOfSlot)){\n" +
                    "                    document.write(\"<td>On Duty</td>\");\n" +
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
            } else if(arrOfTb[k].charAt(1)=='c'){           //Convert a b c to 1 2 3
                arrOfTb[k] = String.valueOf(ka) + "3";
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out=response.getWriter();
        Cookie[] cookies = request.getCookies(); //get webpage.login doctor name from welcome page
        if (cookies != null){
            for (Cookie cookie: cookies){
                if (cookie.getName().equals("firstname")) firstname = cookie.getValue();
                if (cookie.getName().equals("lastname")) lastname = cookie.getValue();
            }
        }
        Cookie firstname_remove = new Cookie("firstname","");
        Cookie lastname_remove = new Cookie("lastname","");
        firstname_remove.setMaxAge(0); lastname_remove.setMaxAge(0);
        response.addCookie(firstname_remove); response.addCookie(lastname_remove);
        response.sendRedirect("webpage.timetable_page");
    }
}
