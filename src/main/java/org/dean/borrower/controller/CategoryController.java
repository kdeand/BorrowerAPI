package org.dean.borrower.controller;

import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.dean.borrower.entity.Category;
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

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {

        List<Category> categories =  categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody @Valid Category category) {

        Category createdCategory = categoryService.createCategory(category);

        return ResponseEntity.status(201).body(createdCategory);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);

        if(category == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody @Valid Category category) {
        Category updatedCategory = categoryService.updateCategory(id, category);

        if(updatedCategory == null) {
            ResponseEntity.notFound().build();
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
