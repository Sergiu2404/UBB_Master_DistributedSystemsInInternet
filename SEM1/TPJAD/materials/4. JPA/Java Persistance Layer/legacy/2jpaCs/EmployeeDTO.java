package p.dtos;
import java.io.*;
public class EmployeeDTO implements Serializable {
    private Long id = -1L;
    private String name = "";
    private int age = -1;
    private String sex = "";
    private Long deptId = -1L;
    public EmployeeDTO() { }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public String toString() {return "<EmplDTO: "+id+" | "+name+" | "+" | "+age+" | "+sex+" | "+deptId+"> ";}
}
