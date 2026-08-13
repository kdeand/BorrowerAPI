package org.dean.borrower.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank
    private User borrower;

    @NotNull
    private LocalDateTime requestDate;

    @NotNull
    private LocalDateTime borrowDate;

    @NotNull
    @FutureOrPresent
    private LocalDateTime expectedReturnDate;

    //it can be null because return date can be added lateron
    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private BorrowRequestStatus status;

}
