package org.dean.borrower.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.dean.borrower.entity.BorrowRequest;
import org.dean.borrower.entity.BorrowRequestItem;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BorrowRequestRequest {

    @NotNull(message = "Borrower ID is required")
    private Long borrowerId;

    @NotNull(message = "Borrow date is required")
    @FutureOrPresent(message = "Borrow date can not be in the past")
    private LocalDateTime borrowDate;

    @NotNull(message = "Expected return date is required")
    @FutureOrPresent(message = "Expected return date can not be in the past")
    private LocalDateTime expectedReturnDate;

    @NotNull
    private List<Long> EquipmentIds;
}


