package org.dean.borrower.service;

import org.dean.borrower.dto.MaintenanceRecordRequest;
import org.dean.borrower.dto.MaintenanceRecordResponse;
import org.dean.borrower.entity.Equipment;
import org.dean.borrower.entity.MaintenanceRecord;
import org.dean.borrower.entity.User;
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
                maintenanceRecord.getCreatedAt()
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

        // Set request data
        maintenanceRecord.setCondition(request.getCondition());
        maintenanceRecord.setNotes(request.getNotes());

        // Server controls creation time
        maintenanceRecord.setCreatedAt(LocalDateTime.now());

        // Save entity
        MaintenanceRecord savedMaintenanceRecord =
                maintenanceRecordRepository.save(maintenanceRecord);

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
}