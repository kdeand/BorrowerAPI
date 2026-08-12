package org.dean.borrower.service;

import org.dean.borrower.entity.BorrowRequest;
import org.dean.borrower.repository.BorrowRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowRequestService {
    private final BorrowRequestRepository borrowRequestRepository;

    public BorrowRequestService(BorrowRequestRepository borrowRequestRepository) {
        this.borrowRequestRepository = borrowRequestRepository;
    }

    public List<BorrowRequest> getAllBorrowRequests() {
        return borrowRequestRepository.findAll();
    }

    public BorrowRequest createBorrowRequest(BorrowRequest borrowRequest) {
        return borrowRequestRepository.save(borrowRequest);
    }

    public BorrowRequest getBorrowRequestById(Long id) {
        return borrowRequestRepository.findById(id).orElse(null);
    }

//    public BorrowRequest updateBorrowRequest(Long id, BorrowRequest newBorrowRequest) {
//        BorrowRequest existingBorrowRequest = borrowRequestRepository.findById(id).orElse(null);
//
//        if (existingBorrowRequest == null) {
//            return null;
//        }
//
//        existingBorrowRequest.setName(newBorrowRequest.getName());
//    }
}
