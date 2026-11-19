package com.qiao.assetmanagement.equipment;

import com.qiao.assetmanagement.equipment.dto.EquipmentRequest;
import com.qiao.assetmanagement.equipment.dto.EquipmentResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public Page<EquipmentResponse> list(
            @RequestParam(required = false) EquipmentStatus status
            , @RequestParam(defaultValue = "0") int page
            , @RequestParam(defaultValue = "10") int size
            , @RequestParam(defaultValue = "asc") String direction
            , @RequestParam(defaultValue = "id") String sortBy
    ) {
        Pageable pageable = PageRequest.of(
                page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        return equipmentService.findAll(status, pageable);
    }

    @GetMapping("/{id}")
    public EquipmentResponse findById(@PathVariable Long id) {
        return equipmentService.findDtoById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EquipmentResponse create(@Valid @RequestBody EquipmentRequest equipmentRequest) {
        return equipmentService.create(equipmentRequest);
    }

    @PutMapping("/{id}")
    public EquipmentResponse update(
            @PathVariable Long id,
            @Valid @RequestBody EquipmentRequest equipmentRequest) {
        return equipmentService.update(id, equipmentRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        equipmentService.delete(id);
    }

}
