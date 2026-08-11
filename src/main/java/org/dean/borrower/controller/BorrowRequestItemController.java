package org.dean.borrower.controller;

import org.dean.borrower.entity.BorrowRequestItem;
import org.dean.borrower.service.BorrowRequestItemService;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public BorrowRequestItem createBorrowRequestItem(@RequestBody BorrowRequestItem borrowRequestItem) {
        return borrowRequestItemService.createBorrowRequestItem(borrowRequestItem);
    }
}
