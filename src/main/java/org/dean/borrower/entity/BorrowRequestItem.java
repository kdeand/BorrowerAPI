package org.dean.borrower.entity;

import jakarta.persistence.*;

@Entity
public class BorrowRequestItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "borrow_request_id")
    private BorrowRequest borrowRequest;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;
}
