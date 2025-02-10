package com.rider.it_request.service;

import com.rider.it_request.dto.CategoryDTO;
import com.rider.it_request.entity.Category;
import com.rider.it_request.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDTO addCategory(CategoryDTO CategoryDTO) {
        Category category = Category.builder()
                .categoryName(CategoryDTO.getCategoryName())
                .build();
        Category savedCategory = categoryRepository.save(category);
        return mapToDTO(savedCategory);
    }

    public Optional<CategoryDTO> updateCategory(int categoryId, CategoryDTO CategoryDTO) {
        if (CategoryDTO.getCategoryName() != null && CategoryDTO.getCategoryName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }

        return categoryRepository.findById(categoryId).map(category -> {
            if (CategoryDTO.getCategoryName() != null) {
                category.setCategoryName(CategoryDTO.getCategoryName());
            }

            Category updatedCategory = categoryRepository.save(category);
            return mapToDTO(updatedCategory);
        });
    }

    public void deleteCategory(int categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new IllegalArgumentException("Category with ID " + categoryId + " does not exist.");
        }
        categoryRepository.deleteById(categoryId);
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(int categoryId) {
        return categoryRepository.findById(categoryId)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Category with ID " + categoryId + " not found"
                ));
    }

    private CategoryDTO mapToDTO(Category category) {
        return new CategoryDTO(
                category.getCategoryId(),
                category.getCategoryName()
        );
    }
}
