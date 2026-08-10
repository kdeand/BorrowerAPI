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
}
