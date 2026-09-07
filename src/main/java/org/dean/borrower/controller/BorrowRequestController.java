package org.dean.borrower.controller;

import jakarta.validation.Valid;
import org.dean.borrower.dto.BorrowRequestRequest;
import org.dean.borrower.dto.BorrowRequestResponse;
import org.dean.borrower.entity.BorrowRequest;
import org.dean.borrower.service.BorrowRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

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
        return ResponseEntity.ok(borrowRequestService.getAllBorrowRequests());
    }


    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<BorrowRequestResponse>
    getBorrowRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(borrowRequestService.getBorrowRequestById(id));
    }


    // CREATE
    @PostMapping
    @PreAuthorize("hasAnyRole('BORROWER', 'ADMIN')")
    public ResponseEntity<BorrowRequestResponse>
    createBorrowRequest(
            @Valid @RequestBody BorrowRequestRequest request) {

        return ResponseEntity
                .status(201)
                .body(borrowRequestService.createBorrowRequest(request));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<BorrowRequestResponse>
    updateBorrowRequest(
            @PathVariable Long id,
            @Valid @RequestBody BorrowRequestRequest request) {
        return ResponseEntity.ok(borrowRequestService.updateBorrowRequest(id, request));
    }


    // DELETE

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteBorrowRequest(@PathVariable Long id) {
        borrowRequestService.deleteBorrowRequest(id);
        return ResponseEntity.noContent().build();
    }

    //approve
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<BorrowRequestResponse> approveBorrowRequest(
            @PathVariable Long id) {
        return ResponseEntity.ok(borrowRequestService.approveBorrowRequest(id));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/deny")
    public ResponseEntity<BorrowRequestResponse> denyBorrowRequest(@PathVariable Long id) {
        return ResponseEntity.ok(borrowRequestService.denyBorrowRequest(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BORROWER')")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<BorrowRequestResponse> cancelBorrowRequest(@PathVariable Long id) {
        BorrowRequestResponse response = borrowRequestService.cancelBorrowRequest(id);
        return ResponseEntity.ok(borrowRequestService.cancelBorrowRequest(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/return")
    public ResponseEntity<BorrowRequestResponse> returnBorrowRequest(@PathVariable Long id) {
        return ResponseEntity.ok(borrowRequestService.returnBorrowRequest(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/borrow")
    public ResponseEntity<BorrowRequestResponse> borrowBorrowRequest(@PathVariable Long id) {
        return ResponseEntity.ok(borrowRequestService.borrowBorrowRequest(id));
    }
}