package vti.department_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class DepartmentDTO {
    private String name;
    private String type;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date createDate;

    @OneToMany(mappedBy = "department")
    private List<AccountDTO> accounts;
}
