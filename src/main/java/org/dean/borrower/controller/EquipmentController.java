package org.dean.borrower.controller;

import org.dean.borrower.entity.Equipment;
import org.dean.borrower.repository.EquipmentRepository;
import org.dean.borrower.service.EquipmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipments")
public class EquipmentController {
    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public List<Equipment> getAllEquipments() {
        return equipmentService.getAllEquipments();
    }

    //for the post method
    @PostMapping
    public Equipment createEquipment(@RequestBody Equipment equipment) {
        return equipmentService.createEquipment(equipment);
    }
}
