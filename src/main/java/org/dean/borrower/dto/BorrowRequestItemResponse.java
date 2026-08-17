package org.dean.borrower.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dean.borrower.entity.BorrowRequest;
import org.dean.borrower.entity.Equipment;

@Getter
@AllArgsConstructor
public class BorrowRequestItemResponse {
    private Long id;
    private Long borrowRequestId;
    private Long equipmentId;
}
