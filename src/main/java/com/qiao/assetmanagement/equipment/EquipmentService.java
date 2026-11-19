package com.qiao.assetmanagement.equipment;

import com.qiao.assetmanagement.common.exception.BusinessRuleException;
import com.qiao.assetmanagement.common.exception.ResourceNotFoundException;
import com.qiao.assetmanagement.employee.EmployeeRepository;
import com.qiao.assetmanagement.equipment.dto.EquipmentMapper;
import com.qiao.assetmanagement.equipment.dto.EquipmentRequest;
import com.qiao.assetmanagement.equipment.dto.EquipmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EquipmentService {

    private EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public Page<EquipmentResponse> findAll(EquipmentStatus status, Pageable pageable) {
        Page<Equipment> page = (status != null) ?
                equipmentRepository.findByStatus(status, pageable) :
                equipmentRepository.findAll(pageable);

        return page.map(EquipmentMapper::toResponse);
    }

    private Equipment findById(Long id) {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", id));
    }

    public EquipmentResponse findDtoById(Long id) {
        Equipment equipment = findById(id);
        return EquipmentMapper.toResponse(equipment);
    }

    public EquipmentResponse create(EquipmentRequest equipmentRequest) {
        if (equipmentRepository.existsByAssetTag(equipmentRequest.assetTag())) {
            throw new BusinessRuleException("Equipment with asset tag " +
                    equipmentRequest.assetTag() + " already exists");
        }

        Equipment equipment = EquipmentMapper.toEntity(equipmentRequest);
        equipment.setStatus(EquipmentStatus.AVAILABLE);// system-controlled
        equipmentRepository.save(equipment);
        return EquipmentMapper.toResponse(equipment);
    }

    public EquipmentResponse update(Long id, EquipmentRequest equipmentRequest) {
        Equipment equipment = findById(id);
        if (!equipment.getAssetTag().equals(equipmentRequest.assetTag())
                && equipmentRepository.existsByAssetTag(equipmentRequest.assetTag())) {
            throw new BusinessRuleException("Equipment with asset tag "
                    + equipmentRequest.assetTag()
                    + " already exists");
        }

        EquipmentMapper.setEntity(equipmentRequest, equipment);
        return EquipmentMapper.toResponse(equipmentRepository.save(equipment));// status untouched
    }

    public void delete(Long id) {
        equipmentRepository.delete(findById(id));
    }
}
