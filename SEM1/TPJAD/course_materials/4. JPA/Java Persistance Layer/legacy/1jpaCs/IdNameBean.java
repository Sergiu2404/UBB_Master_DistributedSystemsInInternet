package p.beans;
import java.util.*;
import javax.ejb.*;
import javax.persistence.*;
import p.entities.IdNameEntity;
import p.dtos.IdNameDTO;
import p.interfaces.IdName;
import p.interfaces.IdNameR;
@Stateless
@Local(IdName.class)
@Remote(IdNameR.class)
public class IdNameBean implements IdName, IdNameR {
    @PersistenceContext(unitName = "1jpa")
    private EntityManager manager;
    public void cud(int id, String name) {
        IdNameEntity ent;
        if (name == null) name = "";
        ent = find(id);
        if (ent == null) {
            if (name.isEmpty()) return;
            ent = new IdNameEntity();
            ent.setId(id);
            ent.setName(name);
            manager.persist(ent);
            return;
        }
        if (name.isEmpty()) {
            manager.remove(ent);
            return;
        }
        ent.setName(name);
        manager.merge(ent);
    }
    public IdNameEntity find(int id) { return manager.find(IdNameEntity.class, id); }
    public List<IdNameEntity> findAll() {
        TypedQuery<IdNameEntity> query = manager.createQuery("select a from IdNameEntity a", IdNameEntity.class);
        return query.getResultList();
    }
    public void cudR(int id, String name) {
        cud(id, name);
    }
    public IdNameDTO findR(int id) { return idName2DTO(find(id)); }
    public List<IdNameDTO> findAllR() {
        List<IdNameEntity> liste = findAll();
        List<IdNameDTO> listd = new ArrayList<IdNameDTO>();
        for (IdNameEntity e : liste) listd.add(idName2DTO(e));
        return listd;
    }
    private IdNameDTO idName2DTO(IdNameEntity e) {
        if (e == null) return null;
        IdNameDTO d = new IdNameDTO();
        d.setId(e.getId());
        d.setName(e.getName());
        return d;
    }
}
