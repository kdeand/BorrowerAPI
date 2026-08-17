package org.dean.borrower.controller;

import jakarta.validation.Valid;
import org.dean.borrower.dto.CategoryRequest;
import org.dean.borrower.dto.CategoryResponse;
import org.dean.borrower.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //READ ALL
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {

        List<CategoryResponse> categories =  categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    //CREATE
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest request) {
        CategoryResponse createdCategory = categoryService.createCategory(request);
        return ResponseEntity.status(201).body(createdCategory);
    }

    //READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        CategoryResponse category = categoryService.getCategoryById(id);

        if(category == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(category);
    }

    //Update
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryRequest request) {
        CategoryResponse updatedCategory = categoryService.updateCategory(id, request);

        if(updatedCategory == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory (@PathVariable Long id) {
        boolean deleted = categoryService.deleteCategory(id);

        if(!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
