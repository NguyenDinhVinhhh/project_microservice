package vti.department_service.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vti.department_service.dto.DepartmentDTO;
import vti.department_service.entity.Department;
import vti.department_service.service.IDepartmentService;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/controller")
public class DepartmentController {
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<DepartmentDTO>getAllDepartment()
    {
        List<Department> departments =  departmentService.getAllDepartment();
        return modelMapper.map(departments,new TypeToken<List<DepartmentDTO>>(){}.getType());
    }
}
