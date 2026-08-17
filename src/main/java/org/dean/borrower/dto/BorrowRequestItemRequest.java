package org.dean.borrower.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.dean.borrower.entity.BorrowRequest;
import org.dean.borrower.entity.Equipment;

@Getter
@Setter
public class BorrowRequestItemRequest {

    @NotNull(message = "Borrow request ID is required")
    private Long borrowRequestId;

    @NotNull(message = "Equipment ID is required")
    private Long equipmentId;

    private String description;

}