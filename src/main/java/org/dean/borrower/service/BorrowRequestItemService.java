package org.dean.borrower.service;

import org.dean.borrower.entity.BorrowRequestItem;
import org.dean.borrower.repository.BorrowRequestItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowRequestItemService {
    private final BorrowRequestItemRepository borrowRequestItemRepository;

    public BorrowRequestItemService(BorrowRequestItemRepository borrowRequestItemRepository) {
        this.borrowRequestItemRepository = borrowRequestItemRepository;
    }

    public List<BorrowRequestItem> getAllBorrowRequestItems() {
        return borrowRequestItemRepository.findAll();
    }

    public BorrowRequestItem createBorrowRequestItem(BorrowRequestItem borrowRequestItem) {
        return borrowRequestItemRepository.save(borrowRequestItem);
    }
}
