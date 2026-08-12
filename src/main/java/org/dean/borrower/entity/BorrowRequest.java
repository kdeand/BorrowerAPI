package org.dean.borrower.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.dean.borrower.enums.BorrowRequestStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "borrow_requests")
@Setter
@Getter
public class BorrowRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //borrowerId
    @ManyToOne
    @JoinColumn(name = "borrower_id")
    private User borrower;

    private LocalDateTime requestDate;
    private LocalDateTime borrowDate;
    private LocalDateTime expectedReturnDate;
    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    private BorrowRequestStatus status;

}
