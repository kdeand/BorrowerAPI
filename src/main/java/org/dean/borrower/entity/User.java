package org.dean.borrower.entity;

import jakarta.persistence.*;
import org.dean.borrower.enums.Role;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String email;
    private String password;
}
