package org.dean.borrower.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.dean.borrower.entity.Equipment;
import org.dean.borrower.entity.User;
import org.dean.borrower.enums.EquipmentCondition;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Setter
public class MaintenanceRecordResponse {
    private Long id;
    private Long equipmentId;
    private Long technicianId;
    private EquipmentCondition condition;
    private String notes;
    private LocalDateTime createdAt;
}
