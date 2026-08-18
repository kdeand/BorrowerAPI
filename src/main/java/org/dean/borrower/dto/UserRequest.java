package org.dean.borrower.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.dean.borrower.enums.Role;

@Getter
@Setter
public class UserRequest {
    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @NotNull
    private Role role;

    @NotNull
    @Email
    private String email;

    @NotBlank
    private String password;

}
