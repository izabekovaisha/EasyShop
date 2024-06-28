package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    // Checks if a product exists in the user's shopping cart
    @Override
    public boolean hasProduct(int userId, int productId) {
        String sql = """
                SELECT COUNT(*) FROM shopping_cart
                WHERE user_id = ? AND product_id = ?
                """;

        try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, productId);

            ResultSet resultSet = statement.executeQuery();

            // Check if there is a result and return true if count > 0
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // return false if no result or error occurred
        return false;
    }

    @Override
    public int getQuantity(int userId, int productId) {
        String sql = """
                SELECT quantity
                FROM shopping_cart
                WHERE user_id = ? AND product_id = ?
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, productId);

            ResultSet resultSet = statement.executeQuery();

            // If a result is found, return the quantity
            if (resultSet.next()) {
                return resultSet.getInt("quantity");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Return 0 if no result found or error occurred
        return 0;
    }

    // Add a product to the shopping cart with default quantity of 1
    @Override
    public ShoppingCart addProduct(int userId, int productId) {
        return addProduct(userId, productId, 1);
    }

    // Add a product to the shopping cart with specified quantity
    @Override
    public ShoppingCart addProduct(int userId, int productId, int quantity) {
        // Checks if the product is already in the cart
        if (hasProduct(userId, productId)) {

            // If product exists, update the quantity
            int currentQuantity = getQuantity(userId, productId);
            return updateProductQuantity(userId, productId, currentQuantity + quantity);

        } else {
            // If product doesn't exist, insert a new record
            String sql = """
                    INSERT INTO shopping_cart (user_id, product_id, quantity)
                    VALUES (?, ?, ?)
                    """;

            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, userId);
                statement.setInt(2, productId);
                statement.setInt(3, quantity);

                statement.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        // Return the updated shopping cart
        return getByUserId(userId);
    }

    @Override
    public ShoppingCart updateProductQuantity(int userId, int productId, int quantity) {
        String sql = """
                    UPDATE shopping_cart
                    SET quantity = ?
                    WHERE user_id = ? AND product_id = ?
                    """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setInt(2, userId);
            statement.setInt(3, productId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Return the updated shopping cart
        return getByUserId(userId);
    }

    @Override
    public ShoppingCart removeProduct(int userId, int productId) {
        String sql = """
                    DELETE FROM shopping_cart
                    WHERE user_id = ? AND product_id = ?
                    """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, productId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Return the updated shopping cart
        return getByUserId(userId);
    }

    @Override
    public ShoppingCart clearCart(int userId) {
        String sql = """
                    DELETE FROM shopping_cart
                    WHERE user_id = ?
                    """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Return the empty shopping cart
        return new ShoppingCart();
    }

    // Get the shopping cart for a specific user
    @Override
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart cart = new ShoppingCart();
        // SQL query to join 'shopping_cart' and 'products' tables
        String sql = """ 
                SELECT * FROM shopping_cart
                JOIN products ON shopping_cart.product_id = products.product_id
                WHERE shopping_cart.user_id = ?
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            // Iterate through results and build the shopping cart
            while (resultSet.next()) {
                ShoppingCartItem item = new ShoppingCartItem();

                // Create a new Product object from the database result
                Product product = MySqlProductDao.mapRow(resultSet);

                // Set the product for this shopping cart item
                item.setProduct(product);

                // Set the quantity of this product in the shopping cart
                // The quantity is retrieved from the 'quantity' column in the database
                item.setQuantity(resultSet.getInt("quantity"));

                cart.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cart;
    }
}
