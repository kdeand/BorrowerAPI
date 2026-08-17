package org.dean.borrower.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.dean.borrower.entity.Equipment;
import org.dean.borrower.entity.User;
import org.dean.borrower.enums.EquipmentCondition;

import java.time.LocalDateTime;

@Getter
@Setter
public class MaintenanceRecordRequest {

    @NotNull
    private Long equipmentId;

    @NotNull
    private Long technicianId;

    @NotNull
    private EquipmentCondition condition;

    private String notes;

    @NotNull
    private LocalDateTime createdAt;
}
