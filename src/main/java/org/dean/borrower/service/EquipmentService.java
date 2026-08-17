package org.dean.borrower.service;

import lombok.AllArgsConstructor;
import org.dean.borrower.dto.EquipmentRequest;
import org.dean.borrower.dto.EquipmentResponse;
import org.dean.borrower.entity.Category;
import org.dean.borrower.entity.Equipment;
import org.dean.borrower.repository.CategoryRepository;
import org.dean.borrower.repository.EquipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final CategoryRepository categoryRepository;

    public EquipmentService(EquipmentRepository equipmentRepository, CategoryRepository categoryRepository) {
        this.equipmentRepository = equipmentRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<EquipmentResponse> getAllEquipments() {
        return equipmentRepository.findAll().stream().map(this::toResponse).toList();
    }

    public EquipmentResponse toResponse(Equipment equipment) {
        return new EquipmentResponse(
                equipment.getId(),
                equipment.getName(),
                equipment.getAssetTag(),
                equipment.getDescription(),
                equipment.getCondition(),
                equipment.getStatus(),
                equipment.getCategory().getId()
        );

    }

    //create
    public EquipmentResponse createEquipment(EquipmentRequest request) {
        //when referencing another table, make sre to include the repository of that table
        Category category = categoryRepository.findById(request.getCategoryId()).orElse(null);

        if(category == null) {
            return null;
        }

        Equipment equipment = new Equipment();

        equipment.setName(request.getName());
        equipment.setCategory(category);
        equipment.setDescription(request.getDescription());
        equipment.setCondition(request.getCondition());
        equipment.setStatus(request.getStatus());
        equipment.setAssetTag(request.getAssetTag());

        Equipment savedEquipment = equipmentRepository.save(equipment);

        return toResponse(savedEquipment);

    }

    public EquipmentResponse getEquipmentById(Long id) {
        Equipment equipment = equipmentRepository.findById(id).orElse(null);

        if (equipment == null) {
            return null;
        }

        return toResponse(equipment);
    }

    //updateEquipment
    public EquipmentResponse updateEquipment(Long id, EquipmentRequest request) {

        //find id trying to
        Equipment existingEquipment = equipmentRepository.findById(id).orElse(null);

        if (existingEquipment == null) {
            return null;
        }

        Category category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if(category == null) {
            return null;
        }

        existingEquipment.setName(request.getName());
        existingEquipment.setAssetTag(request.getAssetTag());
        existingEquipment.setDescription(request.getDescription());
        existingEquipment.setCondition(request.getCondition());
        existingEquipment.setStatus(request.getStatus());
        existingEquipment.setCategory(category);

        Equipment savedEquipment = equipmentRepository.save(existingEquipment);

        return toResponse(savedEquipment);

    }

    public boolean deleteEquipment(Long id) {

        if(equipmentRepository.existsById(id)){
            return false;
        }
        equipmentRepository.deleteById(id);

        return true;
    }
}
