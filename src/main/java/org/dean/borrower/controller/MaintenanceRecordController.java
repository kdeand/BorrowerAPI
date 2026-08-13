package org.dean.borrower.controller;

import com.sun.tools.javac.Main;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.dean.borrower.entity.MaintenanceRecord;
import org.dean.borrower.service.MaintenanceRecordService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<MaintenanceRecord>> getAllMaintenanceRecords() {
        List<MaintenanceRecord> maintenanceRecords = maintenanceRecordService.getAllMaintenanceRecords();
        return ResponseEntity.ok(maintenanceRecords);
    }

    @PostMapping
    public ResponseEntity<MaintenanceRecord> createMaintenanceRecord(@RequestBody @Valid MaintenanceRecord maintenanceRecord) {
        MaintenanceRecord createdMaintenanceRecord = maintenanceRecordService.createMaintenanceRecord(maintenanceRecord);
        return ResponseEntity.status(201).body(createdMaintenanceRecord);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceRecord> getMaintenanceRecordById(@PathVariable Long id) {
        MaintenanceRecord maintenanceRecord = maintenanceRecordService.getMaintenanceRecordById(id);

        if(maintenanceRecord == null) {
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(maintenanceRecord);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceRecord> updateMaintenanceRecord(@PathVariable Long id, @RequestBody @Valid MaintenanceRecord maintenanceRecord) {
        MaintenanceRecord updatedMaintenanceRecord = maintenanceRecordService.updateMaintenanceRecord(id, maintenanceRecord);

        if(updatedMaintenanceRecord == null) {
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedMaintenanceRecord);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenanceRecord(@PathVariable Long id) {
       boolean deleted = maintenanceRecordService.deleteMaintenanceRecord(id);

       if(!deleted) {
           return ResponseEntity.notFound().build();
       }

       return ResponseEntity.noContent().build();
    }
}
