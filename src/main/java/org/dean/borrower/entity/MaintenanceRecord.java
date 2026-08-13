package org.dean.borrower.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.dean.borrower.enums.EquipmentCondition;

import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_record")
@Setter
@Getter
public class MaintenanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //equipmentId
    @ManyToOne
    @JoinColumn(name = "equipment_id")
    @NotNull
    private Equipment equipment;

    //TechnicianId
    @ManyToOne
    @JoinColumn(name = "technician_id")
    @NotNull
    private User technician;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EquipmentCondition condition;

    @NotBlank
    private String notes;

    @NotNull
    private LocalDateTime createdAt;
}
