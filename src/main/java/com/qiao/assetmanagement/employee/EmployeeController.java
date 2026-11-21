package com.qiao.assetmanagement.employee;

import com.qiao.assetmanagement.assignment.AssignmentService;
import com.qiao.assetmanagement.assignment.dto.AssignmentDto;
import com.qiao.assetmanagement.employee.dto.EmployeeRequest;
import com.qiao.assetmanagement.employee.dto.EmployeeDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    private final AssignmentService assignmentService;

    public EmployeeController(EmployeeService employeeService
            , AssignmentService assignmentService) {
        this.employeeService = employeeService;
        this.assignmentService = assignmentService;
    }

    @GetMapping
    public Page<EmployeeDto> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "id") String sort
    ) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(direction), sort));
        return employeeService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public EmployeeDto getEmployee(@PathVariable Long id) {
        return employeeService.findDtoById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDto createEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) {
        return employeeService.create(employeeRequest);
    }

    @PutMapping("/{id}")
    public EmployeeDto updateEmployee(@Valid @RequestBody EmployeeRequest employeeRequest
            , @PathVariable Long id) {

        return employeeService.update(id, employeeRequest);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
    }

    @GetMapping("/{id}/assignments")
    public List<AssignmentDto> assignments(@PathVariable Long id) {
        return assignmentService.currentAssignmentsForEmployee(id);
    }

}
