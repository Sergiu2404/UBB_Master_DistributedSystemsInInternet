package p.interfaces;
import java.util.*;
import p.dtos.IdNameDTO;
public interface IdNameR {
    public void cudR(int id, String name);
    public IdNameDTO findR(int id);
    public List<IdNameDTO> findAllR();
}
