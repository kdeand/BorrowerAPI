package org.dean.borrower.controller;

import org.dean.borrower.entity.BorrowRequestItem;
import org.dean.borrower.service.BorrowRequestItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/borrow_request_item")
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

    @GetMapping("/{id}")
    public BorrowRequestItem getBorrowRequestItemById(@PathVariable Long id) {
        return borrowRequestItemService.getBorrowRequestItemById(id);
    }

    @PutMapping("/{id}")
    public BorrowRequestItem updateBorrowRequestItem(@PathVariable Long id, @RequestBody BorrowRequestItem borrowRequestItem) {
        return borrowRequestItemService.updateBorrowRequestItem(id, borrowRequestItem);
    }
}
