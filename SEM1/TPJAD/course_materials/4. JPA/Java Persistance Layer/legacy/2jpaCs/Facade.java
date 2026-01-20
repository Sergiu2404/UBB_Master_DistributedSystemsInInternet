package p.interfaces;
import java.util.*;
import p.entities.EmployeeEntity;
import p.entities.DepartmentEntity;
import p.dtos.EmployeeDTO;
import p.dtos.DepartmentDTO;
public interface Facade {
    public void createDept(String deptName);
    public void createEmpl(Long id, String name, int age, String sex);
    public DepartmentEntity findDept(Long id);
    public EmployeeEntity findEmpl(Long id);
    public List<DepartmentEntity> findAllDepts();
    public List<EmployeeEntity> findAllEmpls();
    public void createDeptR(String deptName);
    public void createEmplR(Long id, String name, int age, String sex);
    public DepartmentDTO findDeptR(Long id);
    public EmployeeDTO findEmplR(Long id);
    public List<DepartmentDTO> findAllDeptsR();
    public List<EmployeeDTO> findAllEmplsR();
}
