package org.dean.borrower.entity;

import jakarta.persistence.*;
import org.dean.borrower.enums.EquipmentCondition;

import java.time.LocalDateTime;

@Entity
@Table(name = "Maintenance Record")
public class MaintenanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //equipmentId
    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    //TechnicianId
    @ManyToOne
    @JoinColumn(name = "technician_id")
    private User technician;

    @Enumerated(EnumType.STRING)
    private EquipmentCondition condition;
    private String notes;
    private LocalDateTime createdAt;
}
