package org.dean.borrower.controller;

import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.dean.borrower.dto.EquipmentRequest;
import org.dean.borrower.dto.EquipmentResponse;
import org.dean.borrower.entity.Equipment;
import org.dean.borrower.repository.EquipmentRepository;
import org.dean.borrower.service.EquipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {
    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public ResponseEntity<List<EquipmentResponse>> getAllEquipments() {

        List<EquipmentResponse> equipments = equipmentService.getAllEquipments();

        return ResponseEntity.ok(equipments);
    }

    //for the post method
    @PostMapping
    public ResponseEntity<EquipmentResponse> createEquipment(@RequestBody @Valid EquipmentRequest request) {
        EquipmentResponse createdEquipment = equipmentService.createEquipment(request);
        return ResponseEntity.status(201).body(createdEquipment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentResponse> getEquipmentById(@PathVariable Long id){
        EquipmentResponse equipment = equipmentService.getEquipmentById(id);

        if(equipment == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(equipment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentResponse> updateEquipment(@PathVariable Long id, @RequestBody @Valid EquipmentRequest request) {
        EquipmentResponse updatedEquipment = equipmentService.updateEquipment(id, request);

        if(updatedEquipment == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedEquipment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        boolean deleted = equipmentService.deleteEquipment(id);
        if(!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();

    }
}
