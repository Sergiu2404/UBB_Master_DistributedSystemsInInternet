package p.clients;
import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import p.interfaces.Facade;
import p.entities.DepartmentEntity;
import p.entities.EmployeeEntity;
import p.dtos.DepartmentDTO;
import p.dtos.EmployeeDTO;
@WebServlet("/client")
public class ServletClient extends HttpServlet {
    @EJB
    private Facade facade;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
                                             throws ServletException, IOException {
        String deptName = request.getParameter("deptName");
        String createDept = request.getParameter("createDept");
        String emplName = request.getParameter("emplName");
        int age = -1;
        try { age = Integer.parseInt(request.getParameter("age")); } catch(Exception e) {}
        String sex = request.getParameter("sex");
        long id = -1;
        try { id = Long.parseLong(request.getParameter("id")); } catch(Exception e) {}
        String createEmpl = request.getParameter("createEmpl");
        if (createDept != null && deptName != null)
            facade.createDept(deptName);
        else if (createEmpl != null && emplName != null && sex != null && id >= 0)
            facade.createEmpl(id, emplName, age, sex);
        int rows = 5;
        int cols = 14;
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String s = "Departments:\n";
        for (DepartmentEntity d : facade.findAllDepts()) {
            s += d.toString()+"\n";
            rows++;
            cols = Math.max(cols, d.toString().length());
        }
        s += "Employees:\n";
        for (EmployeeEntity e : facade.findAllEmpls()) {
            s += e.toString()+"\n";
            rows++;
            cols = Math.max(cols, e.toString().length());
        }
        s += "DepartmentsDTO:\n";
        for (DepartmentDTO d : facade.findAllDeptsR()) {
            s += d.toString()+"\n";
            rows++;
            cols = Math.max(cols, d.toString().length());
        }
        s += "EmployeesDTO:\n";
        for (EmployeeDTO e : facade.findAllEmplsR()) {
            s += e.toString()+"\n";
            rows++;
            cols = Math.max(cols, e.toString().length());
        }
        cols += 10;
        out.println("<html><head><title>Servlet, EJB, JPA</title></head>");
        out.println("<body><form method=\"post\" action=\"client\"><table>");
        out.println("<tr><td>Depart Name:</td><td>");
        out.println("<input type=\"text\" name=\"deptName\" value=\"\"/></td><td>");
        out.println("<input type=\"submit\" name=\"createDept\" value=\"Create Department\"/></td></tr><tr>");
        out.println("<td>Empl Name:</td><td><input type=\"text\" name=\"emplName\" value=\"\"/>");
        out.println("</td></tr><tr><td>Age:</td><td>");
        out.println("<input type=\"text\" name=\"age\" value=\"\"/>");
        out.println("</td></tr><tr><td>Sex:</td><td>");
        out.println("<input type=\"text\" name=\"sex\" value=\"\"/>");
        out.println("</td></tr><tr><td>Depart Id:</td><td>");
        out.println("<input type=\"text\" name=\"id\" value=\"\"/></td><td>");
        out.println("<input type=\"submit\" name=\"createEmpl\" value=\"Create Employee\"/></td></tr>");
        out.println("<tr><td>All objects:</td></tr>");
        out.println("<tr><td colspan=\"3\"><textarea rows=\""+rows+"\" cols=\""+cols+"\">");
        out.println(s+"</textarea></td></tr></table></form></body></html>");
        out.close();
    }
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
                doPost(request, response);
    }
}
