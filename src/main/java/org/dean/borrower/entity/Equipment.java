package org.dean.borrower.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    private String name;

    @NotBlank
    private String assetTag;

    @NotBlank
    private String description;

    @NotBlank
    private String condition;

    @NotBlank
    private String status;

    //category
    @NotNull
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;




}
