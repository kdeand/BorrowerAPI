package org.dean.borrower.service;

import org.dean.borrower.exception.ResourceNotFoundException;
import org.dean.borrower.repository.BorrowRequestItemRepository;
import org.dean.borrower.repository.BorrowRequestRepository;
import org.dean.borrower.repository.EquipmentRepository;
import org.dean.borrower.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.dean.borrower.entity.User;
import org.dean.borrower.enums.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.util.List;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) //
class BorrowRequestServiceTest {
    @Mock
    private BorrowRequestRepository borrowRequestRepository;

    @Mock
    private BorrowRequestItemRepository borrowRequestItemRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BorrowRequestService borrowRequestService;

    /*aaa pattern, arrange, act, assert*/

    @Test
    void getBorrowRequestById_shouldThrowException_whenNotFound() {

        //ARRANGE
        Long requestId = 999L;
        when(borrowRequestRepository.findById(requestId)).thenReturn(Optional.empty());
        assertThrows(
                ResourceNotFoundException.class, () -> borrowRequestService.getBorrowRequestById(requestId)
        );
    }

    @BeforeEach
    void setUp() {
        User user = new User();

        user.setId(1L);
        user.setEmail("borrower@test.com");
        user.setRole(Role.BORROWER);

        UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            List.of()
                    );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }
}
