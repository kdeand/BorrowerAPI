package org.dean.borrower.Auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dean.borrower.enums.Role;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private Long id;
    private String firstname;
    private String lastname;
    private Role role;
    private String email;
    private String token;
}
