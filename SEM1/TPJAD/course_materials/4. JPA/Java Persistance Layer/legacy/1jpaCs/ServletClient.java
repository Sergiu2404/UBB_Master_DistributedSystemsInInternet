package p.clients;
import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import p.interfaces.IdName;
import p.entities.IdNameEntity;
@WebServlet("/client")
public class ServletClient extends HttpServlet {
    @EJB
    private IdName bean;
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        int id = -1;
        try { id = Integer.parseInt(request.getParameter("id")); } catch (Exception e) {};
        String name = request.getParameter("name");
        if (id >= 0 && name != null) bean.cud(id, name);
        String s = "";
        for (IdNameEntity ine : bean.findAll()) {
            String sd = ine.toString();
            s += sd+"\n";
        }
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println(s);
        out.close();
    }
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
                doPost(request, response);
    }
}
