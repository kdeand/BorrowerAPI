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

    public Equipment getEquipmentById(Long id) {
        return equipmentRepository.findById(id).orElse(null);
    }

    public Equipment updateEquipment(Long id, Equipment newEquipment) {

        Equipment existingEquipment = equipmentRepository.findById(id).orElse(null);

        if (existingEquipment == null) {
            return null;
        }

        existingEquipment.setName(newEquipment.getName());
        existingEquipment.setAssetTag(newEquipment.getAssetTag());
        existingEquipment.setDescription(newEquipment.getDescription());
        existingEquipment.setCondition(newEquipment.getCondition());
        existingEquipment.setStatus(newEquipment.getStatus());
        existingEquipment.setCategory(newEquipment.getCategory());

        return equipmentRepository.save(newEquipment);

    }

    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }
}
