package org.dean.borrower.repository;

import org.dean.borrower.entity.BorrowRequestItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRequestItemRepository extends JpaRepository<BorrowRequestItem, Long> {

    //store the borrowRequestId
    List<BorrowRequestItem> findByBorrowRequestId(Long borrowRequestId);

}
