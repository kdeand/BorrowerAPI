package org.dean.borrower.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class BorrowRequestItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "borrow_request_id")
    private BorrowRequest borrowRequest;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;
}
