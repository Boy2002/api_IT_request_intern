package com.rider.it_request_service.service;

import com.rider.it_request_service.dto.CategoryDTO;
import com.rider.it_request_service.entity.Category;
import com.rider.it_request_service.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoryService {

    @Autowired private CategoryRepository categoryRepository;

    public CategoryDTO addCategory(CategoryDTO CategoryDTO) {
        Category category = Category.builder().categoryName(CategoryDTO.getCategoryName()).build();
        Category savedCategory = categoryRepository.save(category);
        return mapToDTO(savedCategory);
    }

    public Optional<CategoryDTO> updateCategory(int categoryId, CategoryDTO CategoryDTO) {
        if (CategoryDTO.getCategoryName() != null
                && CategoryDTO.getCategoryName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }

        return categoryRepository
                .findById(categoryId)
                .map(
                        category -> {
                            if (CategoryDTO.getCategoryName() != null) {
                                category.setCategoryName(CategoryDTO.getCategoryName());
                            }

                            Category updatedCategory = categoryRepository.save(category);
                            return mapToDTO(updatedCategory);
                        });
    }

    public void deleteCategory(int categoryId) {
        Category category =
                categoryRepository
                        .findById(categoryId)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Category with ID "
                                                        + categoryId
                                                        + " does not exist."));

        category.setDeleted(true); // อัปเดตสถานะเป็นถูกลบ
        categoryRepository.save(category); // บันทึกข้อมูลใหม่
    }

    public List<CategoryDTO> getAllFalseCategories() {
        return categoryRepository.findByIsDeletedFalse().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .sorted(
                        (c1, c2) ->
                                Boolean.compare(
                                        c1.isDeleted(),
                                        c2.isDeleted())) // จัดเรียงให้ False มาก่อน True
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(int categoryId) {
        return categoryRepository
                .findById(categoryId)
                .filter(category -> !category.isDeleted()) // <-- เช็ค isDeleted
                .map(this::mapToDTO)
                .orElseThrow(
                        () ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Category with ID " + categoryId + " not found"));
    }

    public CategoryDTO restoreCategory(int categoryId) {
        Category category =
                categoryRepository
                        .findById(categoryId)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Category with ID " + categoryId + " not found"));

        if (!category.isDeleted()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Category with ID " + categoryId + " is not deleted.");
        }

        category.setDeleted(false); // เปลี่ยนกลับเป็น false
        Category restoredCategory = categoryRepository.save(category);
        return mapToDTO(restoredCategory);
    }

    private CategoryDTO mapToDTO(Category category) {
        return new CategoryDTO(
                category.getCategoryId(), category.getCategoryName(), category.isDeleted());
    }
}
