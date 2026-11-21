package com.qiao.assetmanagement.assignment;

import com.qiao.assetmanagement.assignment.dto.AssignRequest;
import com.qiao.assetmanagement.assignment.dto.AssignmentDto;
import com.qiao.assetmanagement.assignment.projection.EmployeeAssignmentCount;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssignmentDto assign(@Valid @RequestBody AssignRequest assignRequest) {
        return assignmentService.assign(assignRequest.employeeId()
                , assignRequest.equipmentId());
    }

    @PutMapping("/{id}/return")
    public AssignmentDto returnAssignment(
            @PathVariable Long id) {
        return assignmentService.returnAssignment(id);
    }

    @GetMapping("/active")
    public Page<AssignmentDto> active(Pageable pageable) {
        return assignmentService.findActive(pageable);
    }

    @GetMapping("/report/active-by-employee")
    public List<EmployeeAssignmentCount> reportActiveByEmployee() {
        return assignmentService.reportActiveByEmployee();
    }
}
