package org.dean.borrower.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dean.borrower.enums.EquipmentCondition;
import org.dean.borrower.enums.EquipmentStatus;

@Getter
@AllArgsConstructor
public class EquipmentResponse {
    private Long id;
    private String name;
    private String assetTag;
    private String description;
    private EquipmentCondition condition;
    private EquipmentStatus status;
    private Long categoryId;
}
