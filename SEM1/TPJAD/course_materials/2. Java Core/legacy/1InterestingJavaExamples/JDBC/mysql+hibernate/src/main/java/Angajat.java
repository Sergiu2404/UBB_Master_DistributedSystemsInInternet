import javax.persistence.*;
@Entity
@Table
public class Angajat {
    @Id
    @GeneratedValue
    private Integer id;
    // Campuri publice spre a fi vazute direct, fara functii getXXX()
    public String nume;
    public Integer varsta;
    @ManyToOne
    public Sectie sectie;
    public String caracterizare;
    public Angajat() { };
    public Angajat(Integer id, String nume, Integer varsta, Sectie sectie, String caracterizare) {
        this.id = id;
        this.nume = nume;
        this.varsta = varsta;
        this.sectie = sectie;
        this.caracterizare = caracterizare;
    }
    public Integer getId() { return id; }
    public String getNume() { return nume; }
    public Integer getVarsta() { return varsta; }
    public Sectie getSectie() { return sectie; }
    public String getCaracterizare() { return caracterizare; }
    public void setId(Integer id) { if (id != null) this.id = id; }
    public void setNume(String nume) { this.nume = nume.trim(); }
    public void setVarsta(Integer varsta) { this.varsta = varsta; }
    public void setSectie(Sectie sectie) { this.sectie = sectie; }
    public void setCaracterizare(String caracterizare) { this.caracterizare = caracterizare.trim(); }
    public String toString() { return "<"+id+"|"+nume+"|"+varsta+"|"+sectie+"|"+caracterizare+">"; }
}
