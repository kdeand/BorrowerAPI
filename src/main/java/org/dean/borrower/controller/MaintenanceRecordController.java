package org.dean.borrower.controller;

import com.sun.tools.javac.Main;
import org.dean.borrower.entity.MaintenanceRecord;
import org.dean.borrower.service.MaintenanceRecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance_record")
public class MaintenanceRecordController {
    private final MaintenanceRecordService maintenanceRecordService;

    public MaintenanceRecordController(MaintenanceRecordService maintenanceRecordService) {
        this.maintenanceRecordService = maintenanceRecordService;
    }

    @GetMapping
    public List<MaintenanceRecord> getAllMaintenanceRecords() {
        return maintenanceRecordService.getAllMaintenanceRecords();
    }

    @PostMapping
    public MaintenanceRecord createMaintenanceRecord(@RequestBody MaintenanceRecord maintenanceRecord) {
        return maintenanceRecordService.createMaintenanceRecord(maintenanceRecord);
    }

    @GetMapping("/{id}")
    public MaintenanceRecord getMaintenanceRecordById(@PathVariable Long id) {
        return maintenanceRecordService.getMaintenanceRecordById(id);
    }

    @PutMapping("/{id}")
    public MaintenanceRecord updateMaintenanceRecord(@PathVariable Long id, @RequestBody MaintenanceRecord maintenanceRecord) {
        return maintenanceRecordService.updateMaintenanceRecord(id, maintenanceRecord);
    }

    @DeleteMapping("/{id}")
    public void deleteMaintenanceRecord(@PathVariable Long id) {
        maintenanceRecordService.deleteMaintenanceRecord(id);
    }
}
