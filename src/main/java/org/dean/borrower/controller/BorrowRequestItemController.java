package org.dean.borrower.controller;

import jakarta.validation.Valid;
import org.dean.borrower.dto.BorrowRequestItemRequest;
import org.dean.borrower.dto.BorrowRequestItemResponse;
import org.dean.borrower.service.BorrowRequestItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow_request_item")
public class BorrowRequestItemController {

    private final BorrowRequestItemService borrowRequestItemService;

    public BorrowRequestItemController(
            BorrowRequestItemService borrowRequestItemService) {

        this.borrowRequestItemService = borrowRequestItemService;
    }


    // GET ALL
    @GetMapping
    public ResponseEntity<List<BorrowRequestItemResponse>>
    getAllRequestItems() {

        List<BorrowRequestItemResponse> borrowRequestItems =
                borrowRequestItemService.getAllBorrowRequestItems();

        return ResponseEntity.ok(borrowRequestItems);
    }


    // CREATE
    @PostMapping
    public ResponseEntity<BorrowRequestItemResponse>
    createBorrowRequestItem(
            @Valid @RequestBody BorrowRequestItemRequest request) {

        BorrowRequestItemResponse createdBorrowRequestItem =
                borrowRequestItemService.createBorrowRequestItem(request);

        if (createdBorrowRequestItem == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .status(201)
                .body(createdBorrowRequestItem);
    }


    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<BorrowRequestItemResponse>
    getBorrowRequestItemById(@PathVariable Long id) {

        BorrowRequestItemResponse borrowRequestItem =
                borrowRequestItemService.getBorrowRequestItemById(id);

        if (borrowRequestItem == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(borrowRequestItem);
    }


    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<BorrowRequestItemResponse>
    updateBorrowRequestItem(
            @PathVariable Long id,
            @Valid @RequestBody BorrowRequestItemRequest request) {

        BorrowRequestItemResponse updatedBorrowRequestItem =
                borrowRequestItemService.updateBorrowRequestItem(id, request);

        if (updatedBorrowRequestItem == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedBorrowRequestItem);
    }


    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteBorrowRequestItem(@PathVariable Long id) {

        boolean deleted =
                borrowRequestItemService.deleteBorrowRequestItem(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}