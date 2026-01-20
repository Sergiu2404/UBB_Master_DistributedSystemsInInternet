package p.entities;
import javax.persistence.*;
import java.io.*;
import java.util.*;
@Entity
public class Department implements java.io.Serializable {
  private Long id;
  private String name;
  private Collection<Employee> employees;
  @Id
  @GeneratedValue
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="department")
  public Collection<Employee> getEmployees() {
    return employees;
  }
  public void setEmployees(Collection<Employee> employees) {
    this.employees = employees;
  }
  public void addEmployee(String name, int age, String sex) {
    if(employees== null)
      employees = new ArrayList<Employee>();
    Employee employee = new Employee();
    employee.setName(name);
    employee.setAge(age);
    employee.setSex(sex);
    employee.setDepartment(this);
    employees.add(employee);
    setEmployees(employees);
  }
}
