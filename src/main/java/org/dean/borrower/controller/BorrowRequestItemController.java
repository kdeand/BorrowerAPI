package org.dean.borrower.controller;

import org.dean.borrower.entity.BorrowRequestItem;
import org.dean.borrower.service.BorrowRequestItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/requestItems")
@RestController
public class BorrowRequestItemController {
    private final BorrowRequestItemService borrowRequestItemService;

    public BorrowRequestItemController(BorrowRequestItemService borrowRequestItemService) {
        this.borrowRequestItemService = borrowRequestItemService;
    }

    @GetMapping
    public List<BorrowRequestItem> getAllRequestItems() {
        return borrowRequestItemService.getAllBorrowRequestItems();
    }
}
