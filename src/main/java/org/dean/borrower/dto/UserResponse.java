package org.dean.borrower.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.dean.borrower.enums.Role;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String lastname;
    private String firstname;
    private Role role;
    private String email;
}
