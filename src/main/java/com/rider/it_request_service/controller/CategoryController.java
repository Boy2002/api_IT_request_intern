package com.rider.it_request_service.controller;

import com.rider.it_request_service.dto.CategoryDTO;
import com.rider.it_request_service.service.CategoryService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired private CategoryService categoryService;

    // เพิ่ม Category ใหม่
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryDTO> addCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createdCategory = categoryService.addCategory(categoryDTO);
        return ResponseEntity.ok(createdCategory);
    }

    // อัปเดต Category ตาม ID
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(
            @PathVariable int categoryId, @RequestBody CategoryDTO CategoryDTO) {
        try {
            Optional<CategoryDTO> updatedCategory =
                    categoryService.updateCategory(categoryId, CategoryDTO);
            return updatedCategory
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException ex) {
            // กรณีโยน IllegalArgumentException
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // ลบ Category ตาม ID
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable int categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllFalseCategories() {
        List<CategoryDTO> categories = categoryService.getAllFalseCategories();
        return ResponseEntity.ok(categories);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/restore/{categoryId}")
    public ResponseEntity<?> restoreCategory(@PathVariable int categoryId) {
        try {
            CategoryDTO restoredCategory = categoryService.restoreCategory(categoryId);
            return ResponseEntity.ok(restoredCategory);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable int categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }
}
