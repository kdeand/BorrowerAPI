
package org.dean.borrower.service;

import org.dean.borrower.dto.MaintenanceRecordRequest;
import org.dean.borrower.dto.MaintenanceRecordResponse;
import org.dean.borrower.entity.Equipment;
import org.dean.borrower.entity.MaintenanceRecord;
import org.dean.borrower.entity.User;
import org.dean.borrower.enums.EquipmentCondition;
import org.dean.borrower.enums.EquipmentStatus;
import org.dean.borrower.exception.BusinessRuleException;
import org.dean.borrower.exception.ResourceNotFoundException;
import org.dean.borrower.repository.EquipmentRepository;
import org.dean.borrower.repository.MaintenanceRecordRepository;
import org.dean.borrower.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
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
    @Transactional
    public List<MaintenanceRecordResponse> getAllMaintenanceRecords() {

        return maintenanceRecordRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // GET BY ID
    @Transactional
    public MaintenanceRecordResponse getMaintenanceRecordById(Long id) {

        MaintenanceRecord maintenanceRecord =
                maintenanceRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Maintenance record not found with id: " + id
                                )
                        );

        return toResponse(maintenanceRecord);
    }

    // CREATE
    @Transactional
    public MaintenanceRecordResponse createMaintenanceRecord(
            MaintenanceRecordRequest request) {

        // Find equipment
        Equipment equipment = equipmentRepository
                .findById(request.getEquipmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Equipment not found with id: "
                                        + request.getEquipmentId()
                        )
                );

        // Find technician
        User technician = userRepository
                .findById(request.getTechnicianId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Technician not found with id: "
                                        + request.getTechnicianId()
                        )
                );

        // Create maintenance record
        MaintenanceRecord maintenanceRecord = new MaintenanceRecord();

        // Set relationships
        maintenanceRecord.setEquipment(equipment);
        maintenanceRecord.setTechnician(technician);

        // Set request data
        maintenanceRecord.setCondition(request.getCondition());
        maintenanceRecord.setNotes(request.getNotes());

        // Server-controlled values
        maintenanceRecord.setCreatedAt(LocalDateTime.now());

        // Save maintenance record
        MaintenanceRecord savedMaintenanceRecord =
                maintenanceRecordRepository.save(maintenanceRecord);

        // Update equipment status and condition
        equipment.setStatus(EquipmentStatus.MAINTENANCE);
        equipment.setCondition(EquipmentCondition.UNDER_INSPECTION);

        equipmentRepository.save(equipment);

        return toResponse(savedMaintenanceRecord);
    }

    // UPDATE
    @Transactional
    public MaintenanceRecordResponse updateMaintenanceRecord(
            Long id,
            MaintenanceRecordRequest request) {

        // Find existing maintenance record
        MaintenanceRecord existingMaintenanceRecord =
                maintenanceRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Maintenance record not found with id: " + id
                                )
                        );

        // Find equipment
        Equipment equipment = equipmentRepository
                .findById(request.getEquipmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Equipment not found with id: "
                                        + request.getEquipmentId()
                        )
                );

        // Find technician
        User technician = userRepository
                .findById(request.getTechnicianId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Technician not found with id: "
                                        + request.getTechnicianId()
                        )
                );

        // Update existing record
        existingMaintenanceRecord.setEquipment(equipment);
        existingMaintenanceRecord.setTechnician(technician);
        existingMaintenanceRecord.setCondition(request.getCondition());
        existingMaintenanceRecord.setNotes(request.getNotes());

        // Keep original createdAt
        MaintenanceRecord savedMaintenanceRecord =
                maintenanceRecordRepository.save(existingMaintenanceRecord);

        return toResponse(savedMaintenanceRecord);
    }

    // DELETE
    @Transactional
    public void deleteMaintenanceRecord(Long id) {

        MaintenanceRecord maintenanceRecord =
                maintenanceRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Maintenance record not found with id: " + id
                                )
                        );

        maintenanceRecordRepository.delete(maintenanceRecord);
    }

    // EQUIPMENT GOOD
    @Transactional
    public MaintenanceRecordResponse equipmentGood(Long id) {

        MaintenanceRecord maintenanceRecord =
                maintenanceRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Maintenance record not found with id: " + id
                                )
                        );

        if (maintenanceRecord.getCondition()
                != EquipmentCondition.UNDER_INSPECTION) {

            throw new BusinessRuleException(
                    "Only equipment under inspection can be marked as good"
            );
        }

        Equipment equipment = maintenanceRecord.getEquipment();

        equipment.setCondition(EquipmentCondition.GOOD);
        equipment.setStatus(EquipmentStatus.AVAILABLE);

        equipmentRepository.save(equipment);


        maintenanceRecord.setNotes(maintenanceRecord.getNotes());
        maintenanceRecord.setCompletedAt(LocalDateTime.now());
        maintenanceRecord.setCondition(EquipmentCondition.GOOD);

        MaintenanceRecord savedMaintenanceRecord =
                maintenanceRecordRepository.save(maintenanceRecord);


        return toResponse(savedMaintenanceRecord);
    }

    // EQUIPMENT BROKEN
    public MaintenanceRecordResponse equipmentBroken(Long id) {

        MaintenanceRecord maintenanceRecord =
                maintenanceRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Maintenance record not found with id: " + id
                                )
                        );

        if (maintenanceRecord.getCondition()
                != EquipmentCondition.UNDER_INSPECTION) {

            throw new BusinessRuleException(
                    "Only equipment under inspection can be marked as broken"
            );
        }

        Equipment equipment = maintenanceRecord.getEquipment();

        equipment.setCondition(EquipmentCondition.BROKEN);
        equipment.setStatus(EquipmentStatus.ARCHIVED);

        equipmentRepository.save(equipment);

        maintenanceRecord.setCondition(EquipmentCondition.BROKEN);
        maintenanceRecord.setCompletedAt(LocalDateTime.now());

        MaintenanceRecord savedMaintenanceRecord =
                maintenanceRecordRepository.save(maintenanceRecord);

        return toResponse(savedMaintenanceRecord);
    }

    // EQUIPMENT DAMAGED
    @Transactional
    public MaintenanceRecordResponse equipmentDamaged(Long id) {

        MaintenanceRecord maintenanceRecord =
                maintenanceRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Maintenance record not found with id: " + id
                                )
                        );

        if (maintenanceRecord.getCondition()
                != EquipmentCondition.UNDER_INSPECTION) {

            throw new BusinessRuleException(
                    "Only equipment under inspection can be marked as damaged"
            );
        }

        Equipment equipment = maintenanceRecord.getEquipment();

        equipment.setCondition(EquipmentCondition.DAMAGED);
        equipment.setStatus(EquipmentStatus.MAINTENANCE);

        equipmentRepository.save(equipment);

        maintenanceRecord.setCondition(EquipmentCondition.DAMAGED);
        maintenanceRecord.setCompletedAt(LocalDateTime.now());

        MaintenanceRecord savedMaintenanceRecord =
                maintenanceRecordRepository.save(maintenanceRecord);

        return toResponse(savedMaintenanceRecord);
    }
}
