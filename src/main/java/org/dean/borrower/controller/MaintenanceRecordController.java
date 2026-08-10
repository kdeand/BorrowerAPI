package org.dean.borrower.controller;

import org.dean.borrower.entity.MaintenanceRecord;
import org.dean.borrower.service.MaintenanceRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/maintenanceRecords")
public class MaintenanceRecordController {
    private final MaintenanceRecordService maintenanceRecordService;

    public MaintenanceRecordController(MaintenanceRecordService maintenanceRecordService) {
        this.maintenanceRecordService = maintenanceRecordService;
    }

    @GetMapping
    public List<MaintenanceRecord> getAllMaintenanceRecords() {
        return maintenanceRecordService.getAllMaintenanceRecords();
    }
}
