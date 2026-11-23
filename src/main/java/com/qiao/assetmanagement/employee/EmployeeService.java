package com.qiao.assetmanagement.employee;

import com.qiao.assetmanagement.common.exception.BusinessRuleException;
import com.qiao.assetmanagement.common.exception.ResourceNotFoundException;
import com.qiao.assetmanagement.employee.dto.EmployeeMapper;
import com.qiao.assetmanagement.employee.dto.EmployeeRequest;
import com.qiao.assetmanagement.employee.dto.EmployeeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Page<EmployeeResponse> findAll(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(EmployeeMapper::toDto);
    }

    private Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));
    }

    public EmployeeResponse findDtoById(Long id) {
        return EmployeeMapper.toDto(findById(id));
    }

    public EmployeeResponse create(EmployeeRequest employeeRequest) {
        if (employeeRepository.existsByEmail(employeeRequest.email())) {
            throw new BusinessRuleException("Employee with email "
                    + employeeRequest.email() + " already exists");
        }

        Employee employee = EmployeeMapper.toEntity(employeeRequest);

        return EmployeeMapper.toDto(employeeRepository.save(employee));
    }

    public EmployeeResponse update(Long id, EmployeeRequest employeeRequest) {
        Employee employee = findById(id);
        // allow the email to be unchanged, but block it from becoming someone else's
        if (!employee.getEmail().equals(employeeRequest.email())
                && employeeRepository.existsByEmail(employeeRequest.email())) {
            throw new BusinessRuleException("Employee with email "
                    + employeeRequest.email() + " already exists");
        }

        EmployeeMapper.setEntity(employeeRequest, employee);
        return EmployeeMapper.toDto(employeeRepository.save(employee));
    }

    public void delete(Long id) {
        employeeRepository.delete(findById(id));
    }
}
