package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao
{
    boolean hasProduct(int userId, int productId);
    int getQuantity(int userId, int productId);
    ShoppingCart addProduct(int userId, int productId);
    ShoppingCart addProduct(int userId, int productId, int quantity);
    ShoppingCart updateProductQuantity(int userId, int productId, int quantity);
    ShoppingCart removeProduct(int userId, int productId);
    ShoppingCart clearCart(int userId);
    ShoppingCart getByUserId(int userId);

}
