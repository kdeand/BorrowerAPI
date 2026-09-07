package org.dean.borrower.service;
import org.dean.borrower.dto.BorrowRequestRequest;
import org.dean.borrower.dto.BorrowRequestResponse;
import org.dean.borrower.entity.BorrowRequest;
import org.dean.borrower.entity.BorrowRequestItem;
import org.dean.borrower.entity.Equipment;
import org.dean.borrower.entity.User;
import org.dean.borrower.enums.BorrowRequestStatus;
import org.dean.borrower.enums.EquipmentStatus;
import org.dean.borrower.enums.Role;
import org.dean.borrower.exception.ResourceNotFoundException;
import org.dean.borrower.repository.BorrowRequestItemRepository;
import org.dean.borrower.repository.BorrowRequestRepository;
import org.dean.borrower.repository.EquipmentRepository;
import org.dean.borrower.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowRequestService {

    private final BorrowRequestRepository borrowRequestRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final BorrowRequestItemRepository borrowRequestItemRepository;


    public BorrowRequestService(
            BorrowRequestRepository borrowRequestRepository,
            UserRepository userRepository, EquipmentRepository equipmentRepository, BorrowRequestItemRepository borrowRequestItemRepository) {

        this.borrowRequestRepository = borrowRequestRepository;
        this.userRepository = userRepository;
        this.equipmentRepository = equipmentRepository;
        this.borrowRequestItemRepository = borrowRequestItemRepository;
    }

    private User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return (User) authentication.getPrincipal();
    }

    // ENTITY -> RESPONSE DTO
    //toResponse DTO
    private BorrowRequestResponse toResponse(BorrowRequest borrowRequest) {

        List<Long> equipmentIds =
                borrowRequestItemRepository
                        .findByBorrowRequestId(borrowRequest.getId())
                        .stream()
                        .map(item -> item.getEquipment().getId())
                        .toList();

        return new BorrowRequestResponse(
                borrowRequest.getId(),
                borrowRequest.getBorrower().getId(),
                borrowRequest.getRequestDate(),
                borrowRequest.getBorrowDate(),
                borrowRequest.getExpectedReturnDate(),
                borrowRequest.getReturnDate(),
                borrowRequest.getStatus(),
                equipmentIds
        );
    }


    // GET ALL
    //
    public List<BorrowRequestResponse> getAllBorrowRequests() {

        User currentUser = getCurrentUser();

        if(currentUser.getRole() == Role.ADMIN) {
            return borrowRequestRepository
                    .findAll()
                    .stream()
                    .map(this::toResponse)
                    .toList();
        }

        return borrowRequestRepository
                .findByBorrowerId(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // GET BY ID
    public BorrowRequestResponse getBorrowRequestById(Long id) {

        User currentUser = getCurrentUser();

        //exception
        BorrowRequest borrowRequest =
                borrowRequestRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Borrow request not found with this id: " + id
                        )
                );

        if(currentUser.getRole() == Role.ADMIN) {
            return toResponse(borrowRequest);
        }

        if(!borrowRequest.getBorrower().getId().equals(currentUser.getId())) {
            return null;
        }

        return toResponse(borrowRequest);


    }


    // CREATE
    public BorrowRequestResponse createBorrowRequest(
            BorrowRequestRequest request)  {

        User currentUser = getCurrentUser();

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
        borrowRequest.setBorrower(currentUser);
        borrowRequest.setBorrowDate(request.getBorrowDate());
        borrowRequest.setExpectedReturnDate(
                request.getExpectedReturnDate()
        );

//        borrowRequest.setEquipmentIds(request.getEquipmentIds());

        //save the borrowRequestItems in the borrowRequestItems repository;

        // Server-controlled values
        borrowRequest.setRequestDate(LocalDateTime.now());
        borrowRequest.setReturnDate(null);
        borrowRequest.setStatus(BorrowRequestStatus.PENDING);

        BorrowRequest savedBorrowRequest =
                borrowRequestRepository.save(borrowRequest);

        // Save Entity
        //for getting equipment ids and saving them to the borrow request item repository
        for(Long equipmentId : request.getEquipmentIds()) {
            //1. Find equipment, check id
            Equipment eq = equipmentRepository.findById(equipmentId).orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Equipment not found with this id: " + equipmentId
                    )
            );

            //create an entity
            BorrowRequestItem borrowRequestItem = new BorrowRequestItem();

            borrowRequestItem.setEquipment(eq);
            borrowRequestItem.setBorrowRequest(savedBorrowRequest);

            borrowRequestItemRepository.save(borrowRequestItem);
        }

        // Entity -> Response DTO
        return toResponse(savedBorrowRequest);
    }


    // UPDATE
    //Redundant
    public BorrowRequestResponse updateBorrowRequest(
            Long id,
            BorrowRequestRequest request) {

        User currentUser = getCurrentUser();

        BorrowRequest existingBorrowRequest =
                borrowRequestRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Borrow request not found with this id: " + id
                        )
                );

    //Ownership first
        if(currentUser.getRole() != Role.ADMIN) {
            if (!existingBorrowRequest
                    .getBorrower()
                    .getId()
                    .equals(currentUser.getId())) { //current user equals to User currentUser = (User)authentication.getPrincipal();

                return null;
            }
        }


        //exception if expectedreturndate is before borrow date
        if (!request.getExpectedReturnDate()
                .isAfter(request.getBorrowDate())) {

            throw new IllegalArgumentException(
                    "Expected return date must be after borrow date"
            );
        }

        //to update the borrower

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

    //NON-CRUD METHODS: ========================================================================

    //APPROVE
    public BorrowRequestResponse approveBorrowRequest(Long id) {
        //1. Get borrow request ID check if it exists
        //3. if it is pending

        BorrowRequest currentBorrowRequest = borrowRequestRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Borrow request not found with this id: " + id
                )
        );

        //grab the status from the borrowrequeststatus enum
        if (currentBorrowRequest.getStatus() != BorrowRequestStatus.PENDING) {
            return null;
        }

        //get all items belonging in this request
        List<BorrowRequestItem> items = borrowRequestItemRepository.findByBorrowRequestId(id);


        for(BorrowRequestItem item: items) {
            Equipment equipment = item.getEquipment();
            if(equipment.getStatus() != EquipmentStatus.AVAILABLE) {
                return null;
            }
        }

        //traverse to the list
        for(BorrowRequestItem item : items) {
            //getEquipmentIds
            Equipment equipment = item.getEquipment();

            equipment.setStatus(EquipmentStatus.RESERVED);
            equipmentRepository.save(equipment);
        }


        //if valid:
            //getEquipmentIds
        currentBorrowRequest.setStatus(BorrowRequestStatus.APPROVED);
        //change equipment status to reserved


        BorrowRequest approvedBorrowRequest = borrowRequestRepository.save(currentBorrowRequest);

        return toResponse(approvedBorrowRequest);
    }

    //BORROWED
    public BorrowRequestResponse borrowBorrowRequest(Long id) {
        BorrowRequest currentBorrowRequest = borrowRequestRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Borrow request not found with this id: " + id
                )
        );

        //grab the status from the borrowrequeststatus enum
        if (currentBorrowRequest.getStatus() != BorrowRequestStatus.APPROVED) {
            return null;
        }

        List<BorrowRequestItem> items = borrowRequestItemRepository.findByBorrowRequestId(id);

        //traverse
        for(BorrowRequestItem item : items) {
            //check if item exists

            Equipment equipment = item.getEquipment();

            if(equipment.getStatus() != EquipmentStatus.RESERVED) {
                return null;
            }
            //if it's borrowed
            //set equipment status to borrowed

            equipment.setStatus(EquipmentStatus.BORROWED);
        }
        //if valid:

        currentBorrowRequest.setStatus(BorrowRequestStatus.BORROWED);


        BorrowRequest borrowedBorrowRequest = borrowRequestRepository.save(currentBorrowRequest);
        return toResponse(borrowedBorrowRequest);
    }
    //DENY
    public BorrowRequestResponse denyBorrowRequest(Long id) {
        BorrowRequest currentBorrowRequest = borrowRequestRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Borrow request not found with this id: " + id
                )
        );


        //grab the status from the borrowrequeststatus enum
        if (currentBorrowRequest.getStatus() != BorrowRequestStatus.PENDING) {
            return null;
        }

        currentBorrowRequest.setStatus(BorrowRequestStatus.DENIED);

        BorrowRequest deniedBorrowRequest = borrowRequestRepository.save(currentBorrowRequest);
        return toResponse(deniedBorrowRequest);
    }


    //get specific User

    //cancel borrowRequest
    public BorrowRequestResponse cancelBorrowRequest(Long id) {
        BorrowRequest currentBorrowRequest = borrowRequestRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Borrow request not found with this id: " + id
                )
        );

        //grab the status from the borrowrequeststatus enum
        //cant cancel if it's already returned, borrowed, or denied
        if (currentBorrowRequest.getStatus() != BorrowRequestStatus.PENDING && currentBorrowRequest.getStatus() != BorrowRequestStatus.APPROVED) {
            return null;
        }

        User currentUser = getCurrentUser();

        //after finding the request, get the current user
        // If NOT admin, user must own the request

        if (currentUser.getRole() != Role.ADMIN) {
            if (!currentBorrowRequest
                    .getBorrower()
                    .getId()
                    .equals(currentUser.getId())) { //current user equals to User currentUser = (User)authentication.getPrincipal();

                return null;
            }
        }

        //if valid:
        currentBorrowRequest.setStatus(BorrowRequestStatus.CANCELLED);
        BorrowRequest cancelledBorrowRequest = borrowRequestRepository.save(currentBorrowRequest);
        return toResponse(cancelledBorrowRequest);
    }

    //RETURN
    public BorrowRequestResponse returnBorrowRequest(Long id) {
        BorrowRequest currentBorrowRequest = borrowRequestRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Borrow request not found with this id: " + id
                )
        );


        //grab the status from the borrowrequeststatus enum
        if (currentBorrowRequest.getStatus() != BorrowRequestStatus.BORROWED) {
            return null;
        }

        List<BorrowRequestItem> items = borrowRequestItemRepository.findByBorrowRequestId(id);

        for(BorrowRequestItem item : items) {
            Equipment equipment = item.getEquipment();

            if(equipment == null) {
                return null;
            }

            equipment.setStatus(EquipmentStatus.AVAILABLE);
            equipmentRepository.save(equipment);
        }


        //if valid:
        currentBorrowRequest.setReturnDate(LocalDateTime.now());
        currentBorrowRequest.setStatus(BorrowRequestStatus.RETURNED);
        BorrowRequest returnedBorrowRequest = borrowRequestRepository.save(currentBorrowRequest);
        return toResponse(returnedBorrowRequest);
    }

    //RETURN PROCESS
}