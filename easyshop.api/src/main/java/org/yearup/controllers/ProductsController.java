package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Product;
import org.yearup.data.ProductDao;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("products")
@CrossOrigin
public class ProductsController
{
    private ProductDao productDao;

    @Autowired
    public ProductsController(ProductDao productDao)
    {
        this.productDao = productDao;
    }

    //implements the product search/filter functionality BUG 1

    @GetMapping("")
    @PreAuthorize("permitAll()")
    public List<Product> search(@RequestParam(name="cat", required = false) Integer categoryId,
                                @RequestParam(name="minPrice", required = false) BigDecimal minPrice,
                                @RequestParam(name="maxPrice", required = false) BigDecimal maxPrice,
                                @RequestParam(name="color", required = false) String color
    )
    {
        try
        {
            return productDao.search(categoryId, minPrice, maxPrice, color);
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    //      * Handles GET requests to /products/{id}.
    //     * This method implements the requirement to retrieve a single product by its ID,

    @GetMapping("{id}")
    @PreAuthorize("permitAll()")  //// Allows anyone to access this endpoint.
    public Product getById(@PathVariable int id )
    {
        try
        {
            var product = productDao.getById(id);

            if(product == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            return product;
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    //     Handles POST requests to /products.
    //     This method implements the "POST /products"."
    //    Allows administrators to insert new products.

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Product addProduct(@RequestBody Product product)
    {
        try
        {
            return productDao.create(product);
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
    // Handles PUT requests to /products/{id}.
    //     This method implements the "PUT /products/{id}"
    //     Addresses "Bug 2" which involved duplicate products being created instead of updated.
    //     This implementation ensures that an existing product record is modified based on its `product_id`.

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")   /// /**********
    public void updateProduct(@PathVariable int id, @RequestBody Product product)
    {
        try //// Delegates the update operation to the ProductDao.
        {
            productDao.update(id, product);
        }
        catch(Exception ex) // // Catches any unexpected exceptions and returns a 500 Internal Server Error.
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // Handles DELETE requests to /products/{id}.
    // This method implements the "DELETE /products/{id}"
    // Allows administrators to remove products.
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteProduct(@PathVariable int id)
    {
        try
        {
            var product = productDao.getById(id);

    // If the product is not found, throw a 404 Not Found exception.
            if(product == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            productDao.delete(id);
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}