package org.dean.borrower.controller;

import jakarta.validation.Valid;
import org.dean.borrower.dto.MaintenanceRecordRequest;
import org.dean.borrower.dto.MaintenanceRecordResponse;
import org.dean.borrower.entity.MaintenanceRecord;
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

    //METHODS

    //equipmentgood
    //equipmentbroken
    //equipmentdamaged

    @PutMapping("/{id}/good")
    public ResponseEntity<MaintenanceRecordResponse> equipmentGood(@PathVariable Long id) {
        MaintenanceRecordResponse response = maintenanceRecordService.equipmentGood(id);

        if(response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}/broken")
    public ResponseEntity<MaintenanceRecordResponse> equipmentBroken(@PathVariable Long id) {
        MaintenanceRecordResponse response = maintenanceRecordService.equipmentBroken(id);

        if(response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}/damaged")
    public ResponseEntity<MaintenanceRecordResponse> equipmentDamaged(@PathVariable Long id) {
        MaintenanceRecordResponse response = maintenanceRecordService.equipmentDamaged(id);

        if(response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }
}