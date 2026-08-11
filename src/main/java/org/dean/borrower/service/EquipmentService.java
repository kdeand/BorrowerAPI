package org.dean.borrower.service;

import org.dean.borrower.entity.Equipment;
import org.dean.borrower.repository.EquipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;


    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public List<Equipment> getAllEquipments() {
        return equipmentRepository.findAll();
    }

    public Equipment createEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }
}
