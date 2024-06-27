package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("cart")
@PreAuthorize("hasRole('ROLE_USER')") // Ensures only authenticated users can access cart endpoints
@CrossOrigin
public class ShoppingCartController
{
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @GetMapping
    public ShoppingCart getCart(Principal principal)
    {
        try
        {
            String userName = principal.getName(); // Get the currently logged-in username
            User user = userDao.getByUserName(userName); // Find the user by username
            int userId = user.getId(); // Get the user's ID

            return shoppingCartDao.getByUserId(userId);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving shopping cart.");
        }
    }

    @PostMapping("products/{id}")
    public ShoppingCart addToCart(@PathVariable int id, Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            return shoppingCartDao.addProduct(userId, id);
        }
        catch (Exception e)
        {
            throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding product to cart.");
        }
    }

    @PutMapping("products/{id}")
            public ShoppingCart updateCartItem(@PathVariable int id, @RequestBody int quantity, Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

           return shoppingCartDao.updateProductQuantity(userId, id, quantity);
        }
        catch (Exception e)
        {
            throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating cart item.");
        }
    }

    @DeleteMapping("products/{id}")
    public ShoppingCart removeFromCart(@PathVariable int id, Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            return shoppingCartDao.removeProduct(userId, id);
        }
        catch (Exception e)
        {
            throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error removing product from cart.");
        }
    }

    @DeleteMapping
    public ShoppingCart clearCart(Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            return shoppingCartDao.clearCart(userId);
        }
        catch (Exception e)
        {
            throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error clearing shopping cart.");
        }
    }
}
