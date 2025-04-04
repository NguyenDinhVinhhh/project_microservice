package vti.department_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vti.department_service.entity.Department;
import vti.department_service.repository.IDepartmentRepository;

import java.util.List;
@Service
public class DepartmentService implements IDepartmentService{
    @Autowired
    private IDepartmentRepository departmentRepository;
    @Override
    public List<Department> getAllDepartment() {
        return departmentRepository.findAll();
    }
}
