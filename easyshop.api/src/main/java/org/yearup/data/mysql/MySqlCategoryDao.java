package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


//  Implements the CategoryDao interface, providing concrete methods for interacting with the 'categories' table in the database.

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }
//************************************************************************ bug*******************************

    //  This method supports the "GET http://localhost:8080/categories" endpoint
    @Override
    public List<Category> getAllCategories()
    {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT category_id, name, description FROM categories";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet row = statement.executeQuery())
        {
            while (row.next())
            {
                categories.add(mapRow(row));
            }
        }
        catch (SQLException e)
        {
            // Log the exception or rethrow a custom exception
            System.out.println("Error getting all categories: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return categories;
    }

// * This method supports the "GET http://localhost:8080/categories/{id}" endpoint

    @Override
    public Category getById(int categoryId)
    {
        Category category = null;
        String sql = "SELECT category_id, name, description FROM categories WHERE category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, categoryId);
            try (ResultSet row = statement.executeQuery())
            {
                if (row.next())
                {
                    category = mapRow(row);
                }
            }
        }
        catch (SQLException e)
        {
            // Log the exception or rethrow a custom exception
            System.out.println("Error getting category by ID: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return category;
    }

    // --- ADMIN Category Management Implementations


//     Creates a new category in the category table.
//    This method is part of the administrative functionality to manage categories. implements "POST http://localhost:8080/categories" endpoint

    @Override
    public Category create(Category category)
    {
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0)
            {
                try (ResultSet generatedKeys = statement.getGeneratedKeys())
                {
                    if (generatedKeys.next())
                    {
                        category.setCategoryId(generatedKeys.getInt(1));
                        return category;
                    }
                }
            }
        }
        catch (SQLException e)
        {
            // Log the exception or rethrow a custom exception
            System.out.println("Error creating category: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }


//     Updates an existing category in the database.
//     This method is part of the administrative functionality to manage categories.
    // This method implements the "PUT http://localhost:8080/categories/{id}" endpoint

    @Override
    public void update(int categoryId, Category category)
    {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            // Log the exception or rethrow a custom exception
            System.out.println("Error updating category: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


//      Deletes a category from the database.
//      This method is part of the administrative functionality to manage categories.
    // This method implements the "DELETE http://localhost:8080/categories/{id}" endpoint


    @Override
    public void delete(int categoryId)
    {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, categoryId);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            // Log the exception or rethrow a custom exception
            System.out.println("Error deleting category: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    // Helper method to map a ResultSet row to a Category object.
    // This method is used internally by other DAO methods to convert database rows into Java Category objects, making the code cleaner and more reusable.

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}