package org.dean.borrower.controller;

import org.apache.coyote.Response;
import org.dean.borrower.entity.BorrowRequest;
import org.dean.borrower.service.BorrowRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow_requests")
public class BorrowRequestController {
    private final BorrowRequestService borrowRequestService;

    public BorrowRequestController(BorrowRequestService borrowRequestService) {
        this.borrowRequestService = borrowRequestService;
    }

    @GetMapping
    public ResponseEntity<List<BorrowRequest>> getAllBorrowRequests() {

        List<BorrowRequest> borrowRequests = borrowRequestService.getAllBorrowRequests();
        return ResponseEntity.ok(borrowRequests);
    }

    @PostMapping
    public ResponseEntity<BorrowRequest> createBorrowRequest(@RequestBody BorrowRequest borrowRequest) {
        BorrowRequest borrowRequests = borrowRequestService.createBorrowRequest(borrowRequest);

        return ResponseEntity.status(201).body(borrowRequests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowRequest> getBorrowRequestById(@PathVariable Long id) {
        BorrowRequest borrowRequest = borrowRequestService.getBorrowRequestById(id);

        if(borrowRequest == null) {
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(borrowRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BorrowRequest> updateBorrowRequest(@PathVariable Long id, @RequestBody BorrowRequest borrowRequest) {
        BorrowRequest updatedBorrowRequest = borrowRequestService.updateBorrowRequest(id, borrowRequest);

        if(updatedBorrowRequest == null) {
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedBorrowRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrowRequest(@PathVariable Long id) {

        boolean deleted = borrowRequestService.deleteBorrowRequest(id);
        if(!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
