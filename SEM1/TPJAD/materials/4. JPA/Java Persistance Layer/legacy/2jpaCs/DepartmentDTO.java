package p.dtos;
import java.io.*;
import java.util.*;
public class DepartmentDTO implements Serializable {
    private Long id = -1L;
    private String name = "";
    private Collection<EmployeeDTO> empls = (Collection<EmployeeDTO>)(new ArrayList<EmployeeDTO>());
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Collection<EmployeeDTO> getEmpls() { return empls; }
    public void setEmpls(Collection<EmployeeDTO> empls) { this.empls = empls; }
    public String toString() {return "{DeptDTO: "+id+" | "+name+" | "+empls+"} ";}
}
