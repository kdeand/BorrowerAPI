package org.dean.borrower.service;

import org.dean.borrower.entity.MaintenanceRecord;
import org.dean.borrower.repository.MaintenanceRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintenanceRecordService {
    private final MaintenanceRecordRepository maintenanceRecordRepository;

    public MaintenanceRecordService(MaintenanceRecordRepository maintenanceRecordRepository) {
        this.maintenanceRecordRepository = maintenanceRecordRepository;
    }

    public List<MaintenanceRecord> getAllMaintenanceRecords() {
        return maintenanceRecordRepository.findAll();
    }

    public MaintenanceRecord createMaintenanceRecord(MaintenanceRecord maintenanceRecord) {
        return maintenanceRecordRepository.save(maintenanceRecord);
    }

    public MaintenanceRecord getMaintenanceRecordById(Long id) {
        return maintenanceRecordRepository.findById(id).orElse(null);
    }

    public MaintenanceRecord updateMaintenanceRecord(Long id, MaintenanceRecord newMaintenanceRecord) {
        MaintenanceRecord existingMaintenanceRecord = maintenanceRecordRepository.findById(id).orElse(null);
        if(existingMaintenanceRecord == null) {
            return null;
        }

        existingMaintenanceRecord.setEquipment(newMaintenanceRecord.getEquipment());
        existingMaintenanceRecord.setCondition(newMaintenanceRecord.getCondition());
        existingMaintenanceRecord.setTechnician(newMaintenanceRecord.getTechnician());
        existingMaintenanceRecord.setNotes(newMaintenanceRecord.getNotes());
        existingMaintenanceRecord.setCreatedAt(newMaintenanceRecord.getCreatedAt());

        return maintenanceRecordRepository.save(newMaintenanceRecord);
    }

    public void deleteMaintenanceRecord(Long id) {
        maintenanceRecordRepository.deleteById(id);
    }
}
