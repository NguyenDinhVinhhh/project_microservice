package vti.department_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vti.department_service.entity.Department;

@Repository
public interface IDepartmentRepository extends JpaRepository<Department,Integer> {
}
