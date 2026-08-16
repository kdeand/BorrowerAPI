package org.dean.borrower.controller;

import jakarta.validation.Valid;
import org.dean.borrower.dto.BorrowRequestRequest;
import org.dean.borrower.dto.BorrowRequestResponse;
import org.dean.borrower.service.BorrowRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow-requests")
public class BorrowRequestController {

    private final BorrowRequestService borrowRequestService;

    public BorrowRequestController(
            BorrowRequestService borrowRequestService) {

        this.borrowRequestService = borrowRequestService;
    }


    // GET ALL
    @GetMapping
    public ResponseEntity<List<BorrowRequestResponse>>
    getAllBorrowRequests() {

        List<BorrowRequestResponse> borrowRequests =
                borrowRequestService.getAllBorrowRequests();

        return ResponseEntity.ok(borrowRequests);
    }


    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<BorrowRequestResponse>
    getBorrowRequestById(@PathVariable Long id) {

        BorrowRequestResponse borrowRequest =
                borrowRequestService.getBorrowRequestById(id);

        if (borrowRequest == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(borrowRequest);
    }


    // CREATE
    @PostMapping
    public ResponseEntity<BorrowRequestResponse>
    createBorrowRequest(
            @Valid @RequestBody BorrowRequestRequest request) {

        BorrowRequestResponse createdBorrowRequest =
                borrowRequestService.createBorrowRequest(request);

        if (createdBorrowRequest == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .status(201)
                .body(createdBorrowRequest);
    }


    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<BorrowRequestResponse>
    updateBorrowRequest(
            @PathVariable Long id,
            @Valid @RequestBody BorrowRequestRequest request) {

        BorrowRequestResponse updatedBorrowRequest =
                borrowRequestService.updateBorrowRequest(id, request);

        if (updatedBorrowRequest == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedBorrowRequest);
    }


    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteBorrowRequest(@PathVariable Long id) {

        boolean deleted =
                borrowRequestService.deleteBorrowRequest(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}