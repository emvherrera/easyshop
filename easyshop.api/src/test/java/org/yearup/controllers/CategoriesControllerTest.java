package org.yearup.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriesControllerTest {

    @Mock
    private CategoryDao categoryDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private CategoriesController categoriesController;

    private Category sampleCategory;
    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleCategory = new Category(1, "Electronics", "Router");
        sampleProduct = new Product(1, "Router", "WiFi router", 59.99, 1, true, true);
    }

    @Test
    void getAllCategories() {
        List<Category> expectedCategories = Arrays.asList(
                new Category(1, "Electronics", "Router"),
                new Category(2, "School", "Study")
        );

        when(categoryDao.getAllCategories()).thenReturn(expectedCategories);

        List<Category> actualCategories = categoriesController.getAll();

        assertNotNull(actualCategories);
        assertEquals(expectedCategories.size(), actualCategories.size());
        assertEquals(expectedCategories, actualCategories);
        verify(categoryDao, times(1)).getAllCategories();
    }

    @Test
    void getCategoryById_WhenExists() {
        when(categoryDao.getById(1)).thenReturn(sampleCategory);

        Category result = categoriesController.getById(1);

        assertNotNull(result);
        assertEquals(sampleCategory.getName(), result.getName());
        verify(categoryDao, times(1)).getById(1);
    }

    @Test
    void getCategoryById_WhenNotExists_ShouldThrow404() {
        when(categoryDao.getById(999)).thenReturn(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            categoriesController.getById(999);
        });

        verify(categoryDao, times(1)).getById(999);
    }


    @Test
    void getProductsByCategoryId_WhenCategoryNotExists_ShouldThrow404() {
        when(categoryDao.getById(2)).thenReturn(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            categoriesController.getProductsById(2);
        });

    }

    @Test
    void addCategory_ShouldCreateCategory() {
        when(categoryDao.create(sampleCategory)).thenReturn(sampleCategory);

        Category created = categoriesController.addCategory(sampleCategory);

        assertNotNull(created);
        assertEquals(sampleCategory.getName(), created.getName());
        verify(categoryDao).create(sampleCategory);
    }

    @Test
    void updateCategory_WhenIdsMatch_ShouldUpdate() {
        when(categoryDao.getById(1)).thenReturn(sampleCategory);

        Category updatedCategory = new Category(1, "Electronics", "Updated description");
        assertDoesNotThrow(() -> categoriesController.updateCategory(1, updatedCategory));
        verify(categoryDao).update(1, updatedCategory);
    }


    @Test
    void deleteCategory_WhenExists_ShouldDelete() {
        when(categoryDao.getById(1)).thenReturn(sampleCategory);

        assertDoesNotThrow(() -> categoriesController.deleteCategory(1));
        verify(categoryDao).delete(1);
    }

    @Test
    void deleteCategory_WhenNotFound_ShouldThrow404() {
        when(categoryDao.getById(99)).thenReturn(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            categoriesController.deleteCategory(99);
        });


        verify(categoryDao, never()).delete(99);
    }
}
