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

    //get all query
    public List<BorrowRequestItem> getAllBorrowRequestItems() {
        return borrowRequestItemRepository.findAll();
    }

    //create borrow request item
    public BorrowRequestItem createBorrowRequestItem(BorrowRequestItem borrowRequestItem) {
        return borrowRequestItemRepository.save(borrowRequestItem);
    }

    //find by id
    public BorrowRequestItem getBorrowRequestItemById(Long id) {
        return borrowRequestItemRepository.findById(id).orElse(null);

    }

    //update
    public BorrowRequestItem updateBorrowRequestItem(Long id, BorrowRequestItem newBorrowRequestItem) {
        //get the id
        BorrowRequestItem existingBorrowRequestItem = borrowRequestItemRepository.findById(id).orElse(null);

        if(existingBorrowRequestItem == null) {
            return null;
        }

        //else
        existingBorrowRequestItem.setName(newBorrowRequestItem.getName());
        existingBorrowRequestItem.setDescription(newBorrowRequestItem.getDescription());
        existingBorrowRequestItem.setBorrowRequest(newBorrowRequestItem.getBorrowRequest());
        existingBorrowRequestItem.setEquipment(newBorrowRequestItem.getEquipment());

        return borrowRequestItemRepository.save(existingBorrowRequestItem);
    }

    //delete
    public boolean deleteBorrowRequestItem(Long id) {

        if(borrowRequestItemRepository.existsById(id)) {
            return false;
        }
        borrowRequestItemRepository.deleteById(id);
        return true;
    }
}
