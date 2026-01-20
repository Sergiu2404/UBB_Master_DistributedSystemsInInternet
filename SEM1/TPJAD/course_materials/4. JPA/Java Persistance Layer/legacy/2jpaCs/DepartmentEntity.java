package p.entities;
import java.io.*;
import java.util.*;
import javax.persistence.*;
@Entity
public class DepartmentEntity implements Serializable {
    private Long id = 1L; // initializare necesara la GlassFish
    private String name = "";
    private Collection<EmployeeEntity> empls = (Collection<EmployeeEntity>)(new ArrayList<EmployeeEntity>());
    @Id
//    @GeneratedValue // Merge la WildFly
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQMYCLASSID") // se cere la GlassFish
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "dept")
    public Collection<EmployeeEntity> getEmpls() { return empls; }
    public void setEmpls(Collection<EmployeeEntity> empl) { this.empls = empl; }
    public String toString() {return "{Dept: "+id+" | "+name+" | "+empls+"} ";}
}
