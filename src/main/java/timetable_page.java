import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/timetable_page", loadOnStartup = 1)

public class timetable_page extends HttpServlet {
    private static String reqBody;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        PrintWriter out=response.getWriter();
        out.println("<h2>Timetable</h2>");
        final Object[][] table = new String[4][];
        table[0] = new String[] { "foo", "bar", "baz" };
        table[1] = new String[] { "bar2", "foo2", "baz2" };
        table[2] = new String[] { "baz3", "bar3", "foo3" };
        table[3] = new String[] { "foo4", "bar4", "baz4" };

        for (final Object[] row : table) {
            System.out.format("%15s%15s%15s\n", row);
        }
        out.println("<h2>hi there" +reqBody+
                "</h2>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        reqBody=request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        response.sendRedirect("timetable_page");
    }
}
