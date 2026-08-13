package org.dean.borrower.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
