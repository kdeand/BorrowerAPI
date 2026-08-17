package org.dean.borrower.service;

import org.dean.borrower.dto.BorrowRequestItemRequest;
import org.dean.borrower.dto.BorrowRequestItemResponse;
import org.dean.borrower.entity.BorrowRequest;
import org.dean.borrower.entity.BorrowRequestItem;
import org.dean.borrower.entity.Equipment;
import org.dean.borrower.repository.BorrowRequestItemRepository;
import org.dean.borrower.repository.BorrowRequestRepository;
import org.dean.borrower.repository.EquipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowRequestItemService {

    private final BorrowRequestItemRepository borrowRequestItemRepository;
    private final BorrowRequestRepository borrowRequestRepository;
    private final EquipmentRepository equipmentRepository;

    public BorrowRequestItemService(
            BorrowRequestItemRepository borrowRequestItemRepository,
            BorrowRequestRepository borrowRequestRepository,
            EquipmentRepository equipmentRepository) {

        this.borrowRequestItemRepository = borrowRequestItemRepository;
        this.borrowRequestRepository = borrowRequestRepository;
        this.equipmentRepository = equipmentRepository;
    }


    // ENTITY -> RESPONSE DTO
    private BorrowRequestItemResponse toResponse(
            BorrowRequestItem item) {

        return new BorrowRequestItemResponse(
                item.getId(),
                item.getBorrowRequest().getId(),
                item.getEquipment().getId()
        );
    }


    // GET ALL
    public List<BorrowRequestItemResponse> getAllBorrowRequestItems() {

        return borrowRequestItemRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // GET BY ID
    public BorrowRequestItemResponse getBorrowRequestItemById(Long id) {

        BorrowRequestItem item =
                borrowRequestItemRepository.findById(id).orElse(null);
        if (item == null) {
            return null;
        }
        return toResponse(item);
    }


    // CREATE
    public BorrowRequestItemResponse createBorrowRequestItem(
            BorrowRequestItemRequest request) {

        BorrowRequest borrowRequest =
                borrowRequestRepository
                        .findById(request.getBorrowRequestId())
                        .orElse(null);

        if (borrowRequest == null) {
            return null;
        }

        Equipment equipment =
                equipmentRepository
                        .findById(request.getEquipmentId())
                        .orElse(null);

        if (equipment == null) {
            return null;
        }

        BorrowRequestItem item = new BorrowRequestItem();

        item.setBorrowRequest(borrowRequest);
        item.setEquipment(equipment);

        BorrowRequestItem savedItem =
                borrowRequestItemRepository.save(item);

        return toResponse(savedItem);
    }


    // UPDATE
    public BorrowRequestItemResponse updateBorrowRequestItem(
            Long id,
            BorrowRequestItemRequest request) {

        BorrowRequestItem existingItem =
                borrowRequestItemRepository.findById(id).orElse(null);

        if (existingItem == null) {
            return null;
        }

        BorrowRequest borrowRequest =
                borrowRequestRepository
                        .findById(request.getBorrowRequestId())
                        .orElse(null);

        if (borrowRequest == null) {
            return null;
        }

        Equipment equipment =
                equipmentRepository
                        .findById(request.getEquipmentId())
                        .orElse(null);

        if (equipment == null) {
            return null;
        }

        existingItem.setBorrowRequest(borrowRequest);
        existingItem.setEquipment(equipment);

        BorrowRequestItem savedItem =
                borrowRequestItemRepository.save(existingItem);

        return toResponse(savedItem);
    }


    // DELETE
    public boolean deleteBorrowRequestItem(Long id) {

        if (!borrowRequestItemRepository.existsById(id)) {
            return false;
        }
        borrowRequestItemRepository.deleteById(id);

        return true;
    }
}