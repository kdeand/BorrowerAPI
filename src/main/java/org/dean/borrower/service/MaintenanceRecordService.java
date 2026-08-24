package org.dean.borrower.service;

import org.dean.borrower.dto.MaintenanceRecordRequest;
import org.dean.borrower.dto.MaintenanceRecordResponse;
import org.dean.borrower.entity.Equipment;
import org.dean.borrower.entity.MaintenanceRecord;
import org.dean.borrower.entity.User;
import org.dean.borrower.enums.EquipmentCondition;
import org.dean.borrower.enums.EquipmentStatus;
import org.dean.borrower.repository.EquipmentRepository;
import org.dean.borrower.repository.MaintenanceRecordRepository;
import org.dean.borrower.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MaintenanceRecordService {

    private final MaintenanceRecordRepository maintenanceRecordRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;

    public MaintenanceRecordService(
            MaintenanceRecordRepository maintenanceRecordRepository,
            UserRepository userRepository,
            EquipmentRepository equipmentRepository) {

        this.maintenanceRecordRepository = maintenanceRecordRepository;
        this.userRepository = userRepository;
        this.equipmentRepository = equipmentRepository;
    }


    // ENTITY -> RESPONSE DTO
    private MaintenanceRecordResponse toResponse(
            MaintenanceRecord maintenanceRecord) {

        return new MaintenanceRecordResponse(
                maintenanceRecord.getId(),
                maintenanceRecord.getEquipment().getId(),
                maintenanceRecord.getTechnician().getId(),
                maintenanceRecord.getCondition(),
                maintenanceRecord.getNotes(),
                maintenanceRecord.getCreatedAt(),
                maintenanceRecord.getCompletedAt()
        );
    }


    // GET ALL
    public List<MaintenanceRecordResponse> getAllMaintenanceRecords() {

        return maintenanceRecordRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // GET BY ID
    public MaintenanceRecordResponse getMaintenanceRecordById(Long id) {

        MaintenanceRecord maintenanceRecord =
                maintenanceRecordRepository.findById(id).orElse(null);

        if (maintenanceRecord == null) {
            return null;
        }

        return toResponse(maintenanceRecord);
    }


    // CREATE
    public MaintenanceRecordResponse createMaintenanceRecord(
            MaintenanceRecordRequest request) {

        // Find Equipment from equipmentId
        Equipment equipment = equipmentRepository
                .findById(request.getEquipmentId())
                .orElse(null);

        if (equipment == null) {
            return null;
        }

        // Find Technician/User from technicianId
        User technician = userRepository
                .findById(request.getTechnicianId())
                .orElse(null);

        if (technician == null) {
            return null;
        }

        // Create new entity
        MaintenanceRecord maintenanceRecord =
                new MaintenanceRecord();

        // Set relationships
        maintenanceRecord.setEquipment(equipment);
        maintenanceRecord.setTechnician(technician);
        maintenanceRecord.setCreatedAt(LocalDateTime.now());

        // Set request data
        maintenanceRecord.setCondition(request.getCondition());
        maintenanceRecord.setNotes(request.getNotes());

        // Server controls creation time
        maintenanceRecord.setCreatedAt(LocalDateTime.now());

        // Save entity
        MaintenanceRecord savedMaintenanceRecord =
                maintenanceRecordRepository.save(maintenanceRecord);

        //equipment status set
        equipment.setStatus(EquipmentStatus.MAINTENANCE);
        equipment.setCondition(EquipmentCondition.UNDER_INSPECTION);
        // Entity -> Response DTO
        return toResponse(savedMaintenanceRecord);
    }



    // UPDATE
    public MaintenanceRecordResponse updateMaintenanceRecord(
            Long id,
            MaintenanceRecordRequest request) {

        // Find existing maintenance record
        MaintenanceRecord existingMaintenanceRecord =
                maintenanceRecordRepository.findById(id).orElse(null);

        if (existingMaintenanceRecord == null) {
            return null;
        }

        // Find Equipment
        Equipment equipment = equipmentRepository
                .findById(request.getEquipmentId())
                .orElse(null);

        if (equipment == null) {
            return null;
        }

        // Find Technician
        User technician = userRepository
                .findById(request.getTechnicianId())
                .orElse(null);

        if (technician == null) {
            return null;
        }

        // Update existing entity
        existingMaintenanceRecord.setEquipment(equipment);
        existingMaintenanceRecord.setTechnician(technician);
        existingMaintenanceRecord.setCondition(request.getCondition());
        existingMaintenanceRecord.setNotes(request.getNotes());

        // Don't change createdAt during an update

        MaintenanceRecord savedMaintenanceRecord =
                maintenanceRecordRepository.save(existingMaintenanceRecord);

        return toResponse(savedMaintenanceRecord);
    }


    // DELETE
    public boolean deleteMaintenanceRecord(Long id) {

        if (!maintenanceRecordRepository.existsById(id)) {
            return false;
        }

        maintenanceRecordRepository.deleteById(id);

        return true;
    }


    //equipment status ==========================================================================

    //set equipment to be fixed
    public MaintenanceRecordResponse equipmentGood(Long id) {
        MaintenanceRecord maintenanceRecord = maintenanceRecordRepository.findById(id).orElse(null);

        if(maintenanceRecord == null) {
            return null;
        }

        if(maintenanceRecord.getCondition() != EquipmentCondition.UNDER_INSPECTION) {
            return null;
        }

        Equipment equipment = maintenanceRecord.getEquipment();

        equipment.setCondition(EquipmentCondition.GOOD);
        equipment.setStatus(EquipmentStatus.AVAILABLE);
        equipmentRepository.save(equipment);

        //return value for maintenance.
        maintenanceRecord.setNotes(maintenanceRecord.getNotes());
        maintenanceRecord.setCompletedAt(LocalDateTime.now());
        maintenanceRecord.setCondition(EquipmentCondition.GOOD);

        MaintenanceRecord savedMaintenanceRecord = maintenanceRecordRepository.save(maintenanceRecord);
        return toResponse(savedMaintenanceRecord);
    }

    //equipmentBroken
    public MaintenanceRecordResponse equipmentBroken(Long id) {
        MaintenanceRecord maintenanceRecord = maintenanceRecordRepository.findById(id).orElse(null);

        if(maintenanceRecord == null) {
            return null;
        }

        if(maintenanceRecord.getCondition() != EquipmentCondition.UNDER_INSPECTION) {
            return null;
        }

        Equipment equipment = maintenanceRecord.getEquipment();

        equipment.setCondition(EquipmentCondition.BROKEN);
        equipment.setStatus(EquipmentStatus.ARCHIVED);
        equipmentRepository.save(equipment);

        maintenanceRecord.setCondition(EquipmentCondition.BROKEN);
        //notes--- supposedly
        maintenanceRecord.setCompletedAt(LocalDateTime.now());
        //saved
        MaintenanceRecord savedMaintenanceRecord = maintenanceRecordRepository.save(maintenanceRecord);
        return toResponse(savedMaintenanceRecord);
    }

    //equipmentDAMAGED

    public MaintenanceRecordResponse equipmentDamaged(Long id) {
        MaintenanceRecord maintenanceRecord = maintenanceRecordRepository.findById(id).orElse(null);

        if(maintenanceRecord == null) {
            return null;
        }

        if(maintenanceRecord.getCondition() != EquipmentCondition.UNDER_INSPECTION) {
            return null;
        }

        Equipment equipment = maintenanceRecord.getEquipment();

        equipment.setCondition(EquipmentCondition.DAMAGED);
        equipment.setStatus(EquipmentStatus.MAINTENANCE);
        equipmentRepository.save(equipment);

        maintenanceRecord.setCondition(EquipmentCondition.DAMAGED);

        maintenanceRecord.setCompletedAt(LocalDateTime.now());

        //saved
        MaintenanceRecord savedMaintenanceRecord = maintenanceRecordRepository.save(maintenanceRecord);
        return toResponse(savedMaintenanceRecord);
    }



}