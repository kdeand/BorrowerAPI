package org.dean.borrower.service;


import org.dean.borrower.entity.Category;
import org.dean.borrower.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.dean.borrower.dto.CategoryRequest;
import org.dean.borrower.dto.CategoryResponse;

import java.util.List;


@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    //getall
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    //create
    public CategoryResponse createCategory(CategoryRequest request) {

        Category category = new Category();

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category savedCategory = categoryRepository.save(category);

        return toResponse(category);
    }

    //read one
    public CategoryResponse getCategoryById(Long id) {

        Category category =  categoryRepository.findById(id).orElse(null);

        if(category == null) {
            return null;
        }

        return toResponse(category);
    }

    //to update
    public CategoryResponse updateCategory (Long id, CategoryRequest request) {
        //find the id
        Category existingCategory = categoryRepository.findById(id).orElse(null);
        if (existingCategory == null) {
            return null;
        }

        existingCategory.setName(request.getName());
        existingCategory.setDescription(request.getDescription());

        Category savedCategory = categoryRepository.save(existingCategory);
        return toResponse(savedCategory);
    }

    //delete
    public boolean deleteCategory(Long id) {
        if(!categoryRepository.existsById(id)) {
            return false;
        }
        categoryRepository.deleteById(id);

        return true;
    }
}
