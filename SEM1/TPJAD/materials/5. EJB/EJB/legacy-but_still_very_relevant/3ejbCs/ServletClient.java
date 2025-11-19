package p.clients;
import java.io.*;
import javax.ejb.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import p.interfaces.Abac;
import p.interfaces.Counter;
@WebServlet("/client")
public class ServletClient extends HttpServlet {
    @EJB
    private Abac abac;
    @EJB
    private Counter counter;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println("Client EJB + definire EJB-uri independent de AS");
        out.println(ServletClient.class.getName());
        out.println(abac);
        out.println(counter);
        out.println("12 + 13 = " + abac.add(12, 13));
        out.println("12 - 13 = " + abac.sub(12, 13));
        out.println("Urmeaza 10 incrementari, counter dupa incrementare:");
        for (int i = 0; i < 10; i++) {
            counter.inc();
            out.println(counter.getCount());
        }
        out.println("Urmeaza 7 decrementari, counter dupa decrementare:");
        for (int i = 7; i > 0; i--) {
            counter.dec();
            out.println(counter.getCount());
        }
        out.close();
    }
}
