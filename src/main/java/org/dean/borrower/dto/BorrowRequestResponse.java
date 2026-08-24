package org.dean.borrower.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dean.borrower.entity.BorrowRequestItem;
import org.dean.borrower.enums.BorrowRequestStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class BorrowRequestResponse {

    private Long id;

    private Long borrowerId;

    private LocalDateTime requestDate;

    private LocalDateTime borrowDate;

    private LocalDateTime expectedReturnDate;

    private LocalDateTime returnDate;

    private BorrowRequestStatus status;

    private List<Long> EquipmentIds;
}