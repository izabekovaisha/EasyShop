package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
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

    @Override
    public boolean hasProduct(int userId, int productId) {
        String sql = """
                SELECT COUNT(*) FROM shopping_cart
                WHERE user_id = ? AND product_id
                """;

        try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, productId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public int getQuantity(int userId, int productId) {
        String sql = """
                SELECT quantity
                FROM shopping_cart
                WHERE user_id = ? AND product_id
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, productId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("quantity");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public ShoppingCart addProduct(int userId, int productId) {
        return addProduct(userId, productId, 1);
    }

    @Override
    public ShoppingCart addProduct(int userId, int productId, int quantity) {
        if (hasProduct(userId, productId)) {
            int currentQuantity = getQuantity(userId, productId);
            return updateProductQuantity(userId, productId, currentQuantity + quantity);
        } else {
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
        return getByUserId(userId);
    }

    @Override
    public ShoppingCart clearCart(int userId) {
        return null;
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart cart = new ShoppingCart();
        String sql = """ 
                SELECT * FROM shopping_cart
                JOIN products ON shopping_cart.product_id = products.product_id
                WHERE shopping_cart.user_id = ?
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ShoppingCartItem item = new ShoppingCartItem();
                Product product = MySqlProductDao.mapRow(resultSet);




            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cart;
    }
}
