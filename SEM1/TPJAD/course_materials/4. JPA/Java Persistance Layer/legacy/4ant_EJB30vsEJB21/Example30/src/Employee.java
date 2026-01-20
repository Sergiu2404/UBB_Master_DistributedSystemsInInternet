package p.entities;
import javax.persistence.*;
import java.io.*;
@Entity
public class Employee implements Serializable {
  private Long id;
  private String name;
  private int age;
  private String sex;
  private Department department;
  public Employee() {
  }
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
  public int getAge() {
    return age;
  }
  public void setAge(int age) {
    this.age = age;
  }
  public String getSex() {
    return sex;
  }
  public void setSex(String sex) {
    this.sex = sex;
  }
  @ManyToOne
  //@JoinColumn(name="DEPARTMENT_ID")
  public Department getDepartment() {
    return department;
  }
  public void setDepartment(Department aDepartment) {
    this.department = aDepartment;
  }
/*  @PostPersist
  public void samplePostPersist() {
    System.out.println("Added Employee having name = "+ this.getName() +" and Age = "+ this.getAge());
  }*/
}

