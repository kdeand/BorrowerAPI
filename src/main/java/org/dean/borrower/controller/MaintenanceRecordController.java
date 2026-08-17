package org.dean.borrower.controller;

import jakarta.validation.Valid;
import org.dean.borrower.dto.MaintenanceRecordRequest;
import org.dean.borrower.dto.MaintenanceRecordResponse;
import org.dean.borrower.service.MaintenanceRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance_record")
public class MaintenanceRecordController {

    private final MaintenanceRecordService maintenanceRecordService;

    public MaintenanceRecordController(
            MaintenanceRecordService maintenanceRecordService) {

        this.maintenanceRecordService = maintenanceRecordService;
    }


    // GET ALL
    @GetMapping
    public ResponseEntity<List<MaintenanceRecordResponse>>
    getAllMaintenanceRecords() {

        List<MaintenanceRecordResponse> maintenanceRecords =
                maintenanceRecordService.getAllMaintenanceRecords();

        return ResponseEntity.ok(maintenanceRecords);
    }


    // CREATE
    @PostMapping
    public ResponseEntity<MaintenanceRecordResponse>
    createMaintenanceRecord(
            @Valid @RequestBody MaintenanceRecordRequest request) {

        MaintenanceRecordResponse createdMaintenanceRecord =
                maintenanceRecordService.createMaintenanceRecord(request);

        if (createdMaintenanceRecord == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .status(201)
                .body(createdMaintenanceRecord);
    }


    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceRecordResponse>
    getMaintenanceRecordById(@PathVariable Long id) {

        MaintenanceRecordResponse maintenanceRecord =
                maintenanceRecordService.getMaintenanceRecordById(id);

        if (maintenanceRecord == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(maintenanceRecord);
    }


    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceRecordResponse>
    updateMaintenanceRecord(
            @PathVariable Long id,
            @Valid @RequestBody MaintenanceRecordRequest request) {

        MaintenanceRecordResponse updatedMaintenanceRecord =
                maintenanceRecordService.updateMaintenanceRecord(id, request);

        if (updatedMaintenanceRecord == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedMaintenanceRecord);
    }


    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteMaintenanceRecord(@PathVariable Long id) {

        boolean deleted =
                maintenanceRecordService.deleteMaintenanceRecord(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}