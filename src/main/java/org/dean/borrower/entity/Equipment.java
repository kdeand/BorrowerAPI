package org.dean.borrower.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "equipment")
@Setter
@Getter
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String assetTag;
    private String description;
    private String condition;
    private String status;

    //category
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;




}
