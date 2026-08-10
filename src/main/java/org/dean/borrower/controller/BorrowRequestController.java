package org.dean.borrower.controller;

import org.dean.borrower.entity.BorrowRequest;
import org.dean.borrower.service.BorrowRequestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/borrowRequests")
public class BorrowRequestController {
    private final BorrowRequestService borrowRequestService;

    public BorrowRequestController(BorrowRequestService borrowRequestService) {
        this.borrowRequestService = borrowRequestService;
    }

    @GetMapping
    public List<BorrowRequest> getAllBorrowRequests() {
        return borrowRequestService.getAllBorrowRequests();
    }
}
