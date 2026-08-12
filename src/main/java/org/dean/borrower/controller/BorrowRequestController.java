package org.dean.borrower.controller;

import org.dean.borrower.entity.BorrowRequest;
import org.dean.borrower.service.BorrowRequestService;
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
    public List<BorrowRequest> getAllBorrowRequests() {
        return borrowRequestService.getAllBorrowRequests();
    }

    @PostMapping
    public BorrowRequest createBorrowRequest(@RequestBody BorrowRequest borrowRequest) {
        return borrowRequestService.createBorrowRequest(borrowRequest);
    }

    @GetMapping("/{id}")
    public BorrowRequest getBorrowRequestById(@PathVariable Long id) {
        return borrowRequestService.getBorrowRequestById(id);
    }

    @PutMapping("/{id}")
    public BorrowRequest updateBorrowRequest(@PathVariable Long id, @RequestBody BorrowRequest borrowRequest) {
        return borrowRequestService.updateBorrowRequest(id, borrowRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteBorrowRequest(@PathVariable Long id) {
        borrowRequestService.deleteBorrowRequest(id);
    }
}
