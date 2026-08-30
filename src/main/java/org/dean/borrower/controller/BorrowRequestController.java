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

    //approve
    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<BorrowRequestResponse> approveBorrowRequest(
            @PathVariable Long id) {

        BorrowRequestResponse response =
                borrowRequestService.approveBorrowRequest(id);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/deny")
    public ResponseEntity<BorrowRequestResponse> denyBorrowRequest(@PathVariable Long id) {
        BorrowRequestResponse response =
                borrowRequestService.denyBorrowRequest(id);

        if(response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BorrowRequestResponse> cancelBorrowRequest(@PathVariable Long id) {
        BorrowRequestResponse response = borrowRequestService.cancelBorrowRequest(id);

        if(response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<BorrowRequestResponse> returnBorrowRequest(@PathVariable Long id) {
        BorrowRequestResponse response = borrowRequestService.returnBorrowRequest(id);

        if(response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}/borrow")
    public ResponseEntity<BorrowRequestResponse> borrowBorrowRequest(@PathVariable Long id) {
        BorrowRequestResponse response = borrowRequestService.borrowBorrowRequest(id);

        if(response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }
}