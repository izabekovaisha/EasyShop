package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    void addItem(int userId, ShoppingCartItem item);
    void updateItem(int userId, ShoppingCartItem item);
    void clearCart(int userId);
}
