package p.interfaces;
import java.util.*;
import p.entities.IdNameEntity;
public interface IdName {
    public void cud(int id, String name);
    public IdNameEntity find(int id);
    public List<IdNameEntity> findAll();
}
