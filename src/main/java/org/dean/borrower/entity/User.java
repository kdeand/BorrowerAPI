package org.dean.borrower.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.dean.borrower.enums.Role;

@Entity
@Table(name = "users")
@Setter
@Getter
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
