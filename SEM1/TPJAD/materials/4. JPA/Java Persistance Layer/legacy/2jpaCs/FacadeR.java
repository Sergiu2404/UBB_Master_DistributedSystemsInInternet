package p.interfaces;
import java.util.*;
import p.dtos.EmployeeDTO;
import p.dtos.DepartmentDTO;
public interface FacadeR {
    public void createDeptR(String deptName);
    public void createEmplR(Long id, String name, int age, String sex);
    public DepartmentDTO findDeptR(Long id);
    public EmployeeDTO findEmplR(Long id);
    public List<DepartmentDTO> findAllDeptsR();
    public List<EmployeeDTO> findAllEmplsR();
}
