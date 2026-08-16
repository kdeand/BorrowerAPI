package org.dean.borrower.service;

import org.dean.borrower.dto.BorrowRequestRequest;
import org.dean.borrower.dto.BorrowRequestResponse;
import org.dean.borrower.entity.BorrowRequest;
import org.dean.borrower.entity.User;
import org.dean.borrower.enums.BorrowRequestStatus;
import org.dean.borrower.repository.BorrowRequestRepository;
import org.dean.borrower.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowRequestService {

    private final BorrowRequestRepository borrowRequestRepository;
    private final UserRepository userRepository;

    public BorrowRequestService(
            BorrowRequestRepository borrowRequestRepository,
            UserRepository userRepository) {

        this.borrowRequestRepository = borrowRequestRepository;
        this.userRepository = userRepository;
    }


    // ENTITY -> RESPONSE DTO
    private BorrowRequestResponse toResponse(BorrowRequest borrowRequest) {

        return new BorrowRequestResponse(
                borrowRequest.getId(),
                borrowRequest.getBorrower().getId(),
                borrowRequest.getRequestDate(),
                borrowRequest.getBorrowDate(),
                borrowRequest.getExpectedReturnDate(),
                borrowRequest.getReturnDate(),
                borrowRequest.getStatus()
        );
    }


    // GET ALL
    public List<BorrowRequestResponse> getAllBorrowRequests() {

        return borrowRequestRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // GET BY ID
    public BorrowRequestResponse getBorrowRequestById(Long id) {

        BorrowRequest borrowRequest =
                borrowRequestRepository.findById(id).orElse(null);

        if (borrowRequest == null) {
            return null;
        }

        return toResponse(borrowRequest);
    }


    // CREATE
    public BorrowRequestResponse createBorrowRequest(
            BorrowRequestRequest request) {

        // Find the User using borrowerId from the DTO
        User borrower = userRepository
                .findById(request.getBorrowerId())
                .orElse(null);

        if (borrower == null) {
            return null;
        }

        // Business rule:
        // expected return must be AFTER borrow date
        if (!request.getExpectedReturnDate()
                .isAfter(request.getBorrowDate())) {

            throw new IllegalArgumentException(
                    "Expected return date must be after borrow date"
            );
        }

        // Create the database entity
        BorrowRequest borrowRequest = new BorrowRequest();

        // Client-controlled values
        borrowRequest.setBorrower(borrower);
        borrowRequest.setBorrowDate(request.getBorrowDate());
        borrowRequest.setExpectedReturnDate(
                request.getExpectedReturnDate()
        );

        // Server-controlled values
        borrowRequest.setRequestDate(LocalDateTime.now());
        borrowRequest.setReturnDate(null);
        borrowRequest.setStatus(BorrowRequestStatus.PENDING);

        // Save Entity
        BorrowRequest savedBorrowRequest =
                borrowRequestRepository.save(borrowRequest);

        // Entity -> Response DTO
        return toResponse(savedBorrowRequest);
    }


    // UPDATE
    public BorrowRequestResponse updateBorrowRequest(
            Long id,
            BorrowRequestRequest request) {

        BorrowRequest existingBorrowRequest =
                borrowRequestRepository.findById(id).orElse(null);

        if (existingBorrowRequest == null) {
            return null;
        }

        if (!request.getExpectedReturnDate()
                .isAfter(request.getBorrowDate())) {

            throw new IllegalArgumentException(
                    "Expected return date must be after borrow date"
            );
        }

        existingBorrowRequest.setBorrowDate(
                request.getBorrowDate()
        );

        existingBorrowRequest.setExpectedReturnDate(
                request.getExpectedReturnDate()
        );

        BorrowRequest savedBorrowRequest =
                borrowRequestRepository.save(existingBorrowRequest);

        return toResponse(savedBorrowRequest);
    }


    // DELETE
    public boolean deleteBorrowRequest(Long id) {

        if (!borrowRequestRepository.existsById(id)) {
            return false;
        }

        borrowRequestRepository.deleteById(id);

        return true;
    }
}