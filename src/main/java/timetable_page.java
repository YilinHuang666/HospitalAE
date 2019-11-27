import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/timetable", loadOnStartup = 1)

public class timetable_page extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

    }
    private static final Logger log = Logger.getLogger(timetable_page.class.getName());
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        PrintWriter out=response.getWriter();
        out.println("<h2>Timetable</h2>");
        String reqBody=request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        out.println("<h2>" +reqBody+
                "</h2>");
    }
}
