package org.dean.borrower.service;

import org.dean.borrower.dto.BorrowRequestRequest;
import org.dean.borrower.dto.BorrowRequestResponse;
import org.dean.borrower.entity.BorrowRequest;
import org.dean.borrower.entity.BorrowRequestItem;
import org.dean.borrower.entity.Equipment;
import org.dean.borrower.entity.User;
import org.dean.borrower.enums.BorrowRequestStatus;
import org.dean.borrower.enums.EquipmentStatus;
import org.dean.borrower.repository.BorrowRequestItemRepository;
import org.dean.borrower.repository.BorrowRequestRepository;
import org.dean.borrower.repository.EquipmentRepository;
import org.dean.borrower.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
            Equipment eq = equipmentRepository.findById(equipmentId).orElse(null);


            if (eq == null) {
                return null;
            }

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

        //get all items belonging in this request
        List<BorrowRequestItem> items = borrowRequestItemRepository.findByBorrowRequestId(id);

        //traverse to the list
        for(BorrowRequestItem item : items) {
            //getEquipmentIds
            Equipment equipment = item.getEquipment();

            if(equipment.getStatus() != EquipmentStatus.AVAILABLE) {
                return null;
            }

            equipment.setStatus(EquipmentStatus.RESERVED);
            equipmentRepository.save(equipment);
        }

        //if valid:

        currentBorrowRequest.setStatus(BorrowRequestStatus.APPROVED);
        //change equipment status to reserved


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