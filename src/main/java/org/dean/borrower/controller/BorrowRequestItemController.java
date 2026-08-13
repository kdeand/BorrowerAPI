package org.dean.borrower.controller;

import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.dean.borrower.entity.BorrowRequestItem;
import org.dean.borrower.service.BorrowRequestItemService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<BorrowRequestItem>> getAllRequestItems() {
        List<BorrowRequestItem> borrowRequestItems = borrowRequestItemService.getAllBorrowRequestItems();
        return ResponseEntity.ok(borrowRequestItems);
    }

    @PostMapping
    public ResponseEntity<BorrowRequestItem> createBorrowRequestItem(@RequestBody @Valid BorrowRequestItem borrowRequestItem) {
        BorrowRequestItem createdBorrowRequestItem =  borrowRequestItemService.createBorrowRequestItem(borrowRequestItem);
        return ResponseEntity.status(201).body(createdBorrowRequestItem);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowRequestItem> getBorrowRequestItemById(@PathVariable Long id) {
        BorrowRequestItem borrowRequestItem = borrowRequestItemService.getBorrowRequestItemById(id);

        if(borrowRequestItem == null) {
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(borrowRequestItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BorrowRequestItem> updateBorrowRequestItem(@PathVariable Long id, @RequestBody @Valid BorrowRequestItem borrowRequestItem) {

        BorrowRequestItem updatedBorrowRequestItem = borrowRequestItemService.updateBorrowRequestItem(id, borrowRequestItem);
        if(updatedBorrowRequestItem == null) {
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedBorrowRequestItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrowRequestItem(@PathVariable Long id) {
        boolean deleted = borrowRequestItemService.deleteBorrowRequestItem(id);

        if(!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
