import javax.persistence.*;
@Entity
@Table
public class Sectie {
    @Id
    @GeneratedValue
    private Integer id;
    // Campuri publice spre a fi vazute direct, fara functii getXXX()
    public String nume;
    public String descriere;
    public Sectie() { };
    public Sectie(Integer id, String nume, String descriere) {
        this.id = id;
        this.nume = nume;
        this.descriere = descriere;
    }
    public Integer getId() { return id; }
    public String getNume() { return nume; }
    public String getDescriere() { return descriere; }
    public void setId(Integer id) { if (id != null) this.id = id; }
    public void setNume(String nume) { this.nume = nume.trim(); }
    public String toString() { return "["+id+"|"+nume+"|"+descriere+"]"; }
}
