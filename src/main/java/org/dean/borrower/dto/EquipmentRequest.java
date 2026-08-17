package org.dean.borrower.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.dean.borrower.enums.EquipmentCondition;
import org.dean.borrower.enums.EquipmentStatus;

@Getter
@Setter
public class EquipmentRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Asset tag is required")
    private String assetTag;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Condition is required")
    private EquipmentCondition condition;

    @NotBlank(message = "Status is required")
    private EquipmentStatus status;

    @NotNull(message = "Category is required")
    private Long categoryId;
}