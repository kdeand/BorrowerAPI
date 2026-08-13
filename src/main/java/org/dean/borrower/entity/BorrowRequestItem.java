package org.dean.borrower.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class BorrowRequestItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @ManyToOne
    @JoinColumn(name = "borrow_request_id")
    @NotNull
    private BorrowRequest borrowRequest;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    @NotNull
    private Equipment equipment;
}
