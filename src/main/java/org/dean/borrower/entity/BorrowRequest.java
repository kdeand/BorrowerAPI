package org.dean.borrower.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.dean.borrower.enums.BorrowRequestStatus;

import java.time.LocalDateTime;
import java.util.List;

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
    @JoinColumn(name = "borrower_id", nullable = false)
    @NotNull
    private User borrower;

    @NotNull
    private LocalDateTime requestDate;

    @NotNull
    private LocalDateTime borrowDate;

    @NotNull
    private LocalDateTime expectedReturnDate;

    //it can be null because return date can be added later on
    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private BorrowRequestStatus status;

}
