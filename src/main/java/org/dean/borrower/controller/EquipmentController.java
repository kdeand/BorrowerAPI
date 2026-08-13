package org.dean.borrower.controller;

import jakarta.validation.Valid;
import org.apache.coyote.Response;
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
    public ResponseEntity<List<Equipment>> getAllEquipments() {

        List<Equipment> equipments = equipmentService.getAllEquipments();

        return ResponseEntity.ok(equipments);
    }

    //for the post method
    @PostMapping
    public ResponseEntity<Equipment> createEquipment(@RequestBody @Valid Equipment equipment) {
        Equipment createdEquipment = equipmentService.createEquipment(equipment);

        return ResponseEntity.status(201).body(createdEquipment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable Long id){
        Equipment equipment = equipmentService.getEquipmentById(id);

        if(equipment == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(equipment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Equipment> updateEquipment(@PathVariable Long id, @RequestBody @Valid Equipment equipment) {
        Equipment updatedEquipment = equipmentService.updateEquipment(id, equipment);

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
