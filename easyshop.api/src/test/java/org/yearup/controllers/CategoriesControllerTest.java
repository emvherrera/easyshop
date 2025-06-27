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

// This is a Unit Test class for the CategoriesController.
// Unit tests are used to test individual components (units) of your code in isolation.
// Here, we are testing the methods within CategoriesController to ensure they behave as expected.

@ExtendWith(MockitoExtension.class)
// This annotation tells JUnit 5 to enable Mockito for this test class.
// Mockito is a popular mocking framework for Java that helps create "mock" objects.
// Mock objects simulate the behavior of real objects, allowing you to test your code
// without relying on actual database connections or other external dependencies.

class CategoriesControllerTest {

    //    // @Mock creates a mock instance of CategoryDao.
    //    // Instead of using the real CategoryDao (which would connect to a database),
    //    // we use this mock to control its behavior during tests.

    @Mock
    private CategoryDao categoryDao;
// Similarly, @Mock creates a mock instance of ProductDao.
// This allows us to simulate product-related operations without hitting the database.

    @Mock
    private ProductDao productDao;

// @InjectMocks injects the mock objects (categoryDao and productDao) into
// the CategoriesController instance. This means that when we call methods
// on 'categoriesController' in our tests, it will use our mock DAOs instead of real ones.

    @InjectMocks
    private CategoriesController categoriesController;
    // These are sample data objects that we'll use in our tests.
    // It's good practice to define sample data that represents typical scenarios.

    private Category sampleCategory;
    private Product sampleProduct;

    // The @BeforeEach annotation means this method will run before *each* test method.It's used to set up common test data or configurations, ensuring each test starts with a clean and predictable state.

    @BeforeEach
    void setUp() {
        sampleCategory = new Category(1, "Electronics", "Router");
        sampleProduct = new Product(1, "Router", "WiFi router", 59.99, 1, true, true);
    }

// * Test case for the `getAll()` method in CategoriesController.
// * This test verifies that the controller correctly retrieves all categories.

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

    //Test case for `getById()` when the category exists.
    //Verifies that the controller returns the correct category when found.
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
