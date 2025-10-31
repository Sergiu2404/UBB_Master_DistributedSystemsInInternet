package p.entities;
import java.io.*;
import javax.persistence.*;
@Entity
public class EmployeeEntity implements Serializable {
    private Long id = 123456L; // initializare necesara la GlassFish
    private String name = "";
    private int age = -1;
    private String sex = "";
    private DepartmentEntity dept = null;
    public EmployeeEntity() { }
    @Id
//    @GeneratedValue // merge la WildFly
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQMYCLASSID") // se cere la GlassFish
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    @ManyToOne
    // @JoinColumn(name="DEPARTMENT_ID")
    public DepartmentEntity getDept() { return dept; }
    public void setDept(DepartmentEntity dept) { this.dept = dept; }
    /*
     * @PostPersist public void samplePostPersist() {
     * System.out.println("Added Employee having name = "+ this.getName()
     * +" and Age = "+ this.getAge()); }
     */
    public String toString() {return "<Empl: "+id+" | "+name+" | "+" | "+age+" | "+sex+" | "+dept.getId()+"> ";}
}
