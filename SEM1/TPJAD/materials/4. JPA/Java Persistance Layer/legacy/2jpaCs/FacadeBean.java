package p.beans;
import javax.ejb.*;
import javax.persistence.*;
import java.util.*;
import p.interfaces.Facade;
import p.interfaces.FacadeR;
import p.entities.DepartmentEntity;
import p.entities.EmployeeEntity;
import p.dtos.DepartmentDTO;
import p.dtos.EmployeeDTO;
@Stateless
@Local(Facade.class)
@Remote(FacadeR.class)
public class FacadeBean implements Facade, FacadeR {
    @PersistenceContext(unitName = "2jpa")
    private EntityManager manager;
    public void createDept(String deptName) {
        DepartmentEntity dept = new DepartmentEntity();
        dept.setName(deptName);
        manager.persist(dept);
    }
    public void createEmpl(Long deptId, String name, int age, String sex) {
        EmployeeEntity empl = new EmployeeEntity();
        empl.setName(name);
        empl.setAge(age);
        empl.setSex(sex);
        DepartmentEntity dept = findDept(deptId);
        if (dept != null) {
            empl.setDept(dept);
            Collection<EmployeeEntity> empls = dept.getEmpls();
            empls.add(empl);
            dept.setEmpls(empls);
            manager.merge(dept);
        }
        manager.persist(dept);
    }
    public DepartmentEntity findDept(Long id) {
        return manager.find(DepartmentEntity.class, id);
    }
    public EmployeeEntity findEmpl(Long id) {
        return manager.find(EmployeeEntity.class, id);
    }
    public List<DepartmentEntity> findAllDepts() {
        TypedQuery<DepartmentEntity> query = manager.createQuery(
            "select d from DepartmentEntity d ", DepartmentEntity.class);
        return query.getResultList();
    }
    public List<EmployeeEntity> findAllEmpls() {
        TypedQuery<EmployeeEntity> query = manager.createQuery(
            "select e from EmployeeEntity e ", EmployeeEntity.class);
        return query.getResultList();
    }
    public void createDeptR(String deptName) { createDept(deptName); }
    public void createEmplR(Long id, String name, int age, String sex) { createEmpl(id, name, age, sex); }
    public DepartmentDTO findDeptR(Long id) { return dept2DTO(findDept(id)); }
    public EmployeeDTO findEmplR(Long id) { return empl2DTO(findEmpl(id)); }
    public List<EmployeeDTO> findAllEmplsR() {
        List<EmployeeDTO> empls = (List<EmployeeDTO>)(new ArrayList<EmployeeDTO>());
        for (EmployeeEntity empl : findAllEmpls()) {
            empls.add(empl2DTO(empl));
        }
        return empls;
    }
    public List<DepartmentDTO> findAllDeptsR() {
        List<DepartmentDTO> depts = (List<DepartmentDTO>)(new ArrayList<DepartmentDTO>());
        for (DepartmentEntity dept : findAllDepts()) {
            depts.add(dept2DTO(dept));
        }
        return depts;
    }
    private EmployeeDTO empl2DTO(EmployeeEntity empl) {
        if (empl == null) return null;
        EmployeeDTO emplDTO = new EmployeeDTO();
        emplDTO.setId(empl.getId());
        emplDTO.setName(empl.getName());
        emplDTO.setAge(empl.getAge());
        emplDTO.setSex(empl.getSex());
        emplDTO.setDeptId((empl.getDept()).getId());
        return emplDTO;
    }
    private DepartmentDTO dept2DTO(DepartmentEntity dept) {
        if (dept == null) return null;
        DepartmentDTO deptDTO = new DepartmentDTO();
        deptDTO.setId(dept.getId());
        deptDTO.setName(dept.getName());
        for (EmployeeEntity empl : dept.getEmpls()) {
            deptDTO.getEmpls().add(empl2DTO(empl));
        }
        return deptDTO;
    }
}
