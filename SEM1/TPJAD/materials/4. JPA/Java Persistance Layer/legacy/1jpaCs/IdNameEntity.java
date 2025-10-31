package p.entities;
import java.io.*;
import javax.persistence.*;
@Entity
public class IdNameEntity implements Serializable {
    private int id;
    private String name;
    @Id
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String toString() { return "[ "+id+" | "+name+" ]"; }
}
