package org.dean.borrower.service;

import org.dean.borrower.dto.BorrowRequestRequest;
import org.dean.borrower.dto.BorrowRequestResponse;
import org.dean.borrower.entity.BorrowRequest;
import org.dean.borrower.entity.User;
import org.dean.borrower.enums.BorrowRequestStatus;
import org.dean.borrower.repository.BorrowRequestRepository;
import org.dean.borrower.repository.EquipmentRepository;
import org.dean.borrower.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowRequestService {


    private final BorrowRequestRepository borrowRequestRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;

    public BorrowRequestService(
            BorrowRequestRepository borrowRequestRepository,
            UserRepository userRepository, EquipmentRepository equipmentRepository) {

        this.borrowRequestRepository = borrowRequestRepository;
        this.userRepository = userRepository;
        this.equipmentRepository = equipmentRepository;
    }


    // ENTITY -> RESPONSE DTO
    //toResponse DTO
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
            BorrowRequestRequest request)  {

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
        //exception
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

        //exception if expectedreturndate is before borrow date
        if (!request.getExpectedReturnDate()
                .isAfter(request.getBorrowDate())) {

            throw new IllegalArgumentException(
                    "Expected return date must be after borrow date"
            );
        }

        //to update the borrower
        User borrower = userRepository.findById(request.getBorrowerId()).orElse(null);
        if(borrower == null) {
            return null;
        }

        existingBorrowRequest.setBorrowDate(
                request.getBorrowDate()
        );

        existingBorrowRequest.setExpectedReturnDate(
                request.getExpectedReturnDate()
        );

        existingBorrowRequest.setBorrower(borrower);

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

    //NON-CRUD METHODS: ========================================================================

    //APPROVE
    public BorrowRequestResponse approveBorrowRequest(Long id) {
        //1. Get borrow request ID check if it exists
        //3. if it is pending

        BorrowRequest currentBorrowRequest = borrowRequestRepository.findById(id).orElse(null);

        if (currentBorrowRequest == null) {
            return null;
        }

        //grab the status from the borrowrequeststatus enum
        if (currentBorrowRequest.getStatus() != BorrowRequestStatus.PENDING) {
            return null;
        }

        //if valid:

        currentBorrowRequest.setStatus(BorrowRequestStatus.APPROVED);


        BorrowRequest approvedBorrowRequest = borrowRequestRepository.save(currentBorrowRequest);
        return toResponse(approvedBorrowRequest);
    }

    //BORROWED
    public BorrowRequestResponse borrowBorrowRequest(Long id) {
        BorrowRequest currentBorrowRequest = borrowRequestRepository.findById(id).orElse(null);

        if (currentBorrowRequest == null) {
            return null;
        }

        //grab the status from the borrowrequeststatus enum
        if (currentBorrowRequest.getStatus() != BorrowRequestStatus.APPROVED) {
            return null;
        }

        //if valid:

        currentBorrowRequest.setStatus(BorrowRequestStatus.BORROWED);


        BorrowRequest borrowedBorrowRequest = borrowRequestRepository.save(currentBorrowRequest);
        return toResponse(borrowedBorrowRequest);
    }
    //DENY
    public BorrowRequestResponse denyBorrowRequest(Long id) {
        BorrowRequest currentBorrowRequest = borrowRequestRepository.findById(id).orElse(null);

        if (currentBorrowRequest == null) {
            return null;
        }

        //grab the status from the borrowrequeststatus enum
        if (currentBorrowRequest.getStatus() != BorrowRequestStatus.PENDING) {
            return null;
        }

        currentBorrowRequest.setStatus(BorrowRequestStatus.DENIED);

        BorrowRequest deniedBorrowRequest = borrowRequestRepository.save(currentBorrowRequest);
        return toResponse(deniedBorrowRequest);
    }

    //cancel borrowRequest
    public BorrowRequestResponse cancelBorrowRequest(Long id) {
        BorrowRequest currentBorrowRequest = borrowRequestRepository.findById(id).orElse(null);

        if (currentBorrowRequest == null) {
            return null;
        }

        //grab the status from the borrowrequeststatus enum
        //cant cancel if it's already returned, borrowed, or denied
        if (currentBorrowRequest.getStatus() != BorrowRequestStatus.PENDING && currentBorrowRequest.getStatus() != BorrowRequestStatus.APPROVED) {
            return null;
        }

        //if valid:
        currentBorrowRequest.setStatus(BorrowRequestStatus.CANCELLED);
        BorrowRequest cancelledBorrowRequest = borrowRequestRepository.save(currentBorrowRequest);
        return toResponse(cancelledBorrowRequest);
    }

    //RETURN
    public BorrowRequestResponse returnBorrowRequest(Long id) {
        BorrowRequest currentBorrowRequest = borrowRequestRepository.findById(id).orElse(null);

        if (currentBorrowRequest == null) {
            return null;
        }

        //grab the status from the borrowrequeststatus enum
        if (currentBorrowRequest.getStatus() != BorrowRequestStatus.BORROWED) {
            return null;
        }


        //if valid:
        currentBorrowRequest.setReturnDate(LocalDateTime.now());
        currentBorrowRequest.setStatus(BorrowRequestStatus.RETURNED);
        BorrowRequest returnedBorrowRequest = borrowRequestRepository.save(currentBorrowRequest);
        return toResponse(returnedBorrowRequest);
    }
}