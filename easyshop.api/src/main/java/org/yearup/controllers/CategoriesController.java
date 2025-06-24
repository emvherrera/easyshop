package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

@RestController
// for managing categories and their associated products.
//  This controller handles HTTP requests related to categories, providing endpoints for retrieving, adding, updating, and deleting category data.
@RequestMapping
// Maps all requests starting with /categories to methods in this controller.

@CrossOrigin
// Allows cross-origin requests from any domain, useful for front-end applications running on a different port/domain.
public class CategoriesController
{
    private CategoryDao categoryDao;
    private ProductDao productDao;
    /**
     * Constructor for CategoriesController.
     * Spring's @Autowired annotation automatically injects instances of CategoryDao
     * and ProductDao when the controller is created. This is known as Dependency Injection.
     * @param categoryDao The DAO for categories.
     * @param productDao The DAO for products.
     */
    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao)
    {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }


    // create an Autowired controller to inject the categoryDao and ProductDao
@Autowired
    // add the appropriate annotation for a get action
    public List<Category> getAll()
    {
        // find and return all categories
        return categoryDao.getAllCategories();
    }

    // add the appropriate annotation for a get action
    public Category getById(@PathVariable int id)
    {
        // get the category by id
        return null;
    }

  //      * Handles GET requests to /categories.
  //     * Returns a list of all categories in the system.
  //     * @return A List of Category objects.

    @GetMapping("{categoryId}/products")
    //Maps HTTP GET request to /categories to this method

    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        // get a list of product by categoryId
        return null;
    }


    @PostMapping
    // add annotation to call this method for a POST action
    // add annotation to ensure that only an ADMIN can call this function
    public Category addCategory(@RequestBody Category category)
    {
        // insert the category
        return null;
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    public void updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        // update the category by id
    }


    @DeleteMapping
    // @DeleteMapping("/{id}") // Maps HTTP DELETE requests to /categories/{id} to this method.
    @PreAuthorize("hasRole('ADMIN')")
    // @PreAuthorize("hasRole('ADMIN')") // Requires the authenticated user to have the 'ADMIN' role.
    @ResponseStatus
    //@ResponseStatus(HttpStatus.NO_CONTENT) // Sets the HTTP response status to 204 No Content for a successful deletion.
    public void deleteCategory(@PathVariable int id)
    {
        // Check if the category exists before attempting to delete.
        Category existingCategory = categoryDao.getById(id);
        if (existingCategory == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with ID: " + id);
        }
        // Delete the category by id using the CategoryDao.
        categoryDao.delete(id);
    }
}
