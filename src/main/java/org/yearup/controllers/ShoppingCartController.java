package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("cart")
@PreAuthorize("hasRole('ROLE_USER')")
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
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            return shoppingCartDao.getByUserId(userId);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving shopping cart.");
        }
    }

    @PostMapping("products/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addToCart(@PathVariable int productId, Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            Product product = productDao.getById(productId);
            if (product == null )
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            ShoppingCartItem item = new ShoppingCartItem();
            item.setProduct(product);
            item.setQuantity(1);

            shoppingCartDao.addItem(userId, item);
        }
        catch (Exception e)
        {
            throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding product to cart.");
        }
    }

    @PutMapping("products/{id}")
            public void updateCartItem(@PathVariable int productId, @RequestBody ShoppingCartItem item, Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            Product product = productDao.getById(productId);
            if (product == null )
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            item.setProduct(product);
            shoppingCartDao.updateItem(userId, item);
        }
        catch (Exception e)
        {
            throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating cart item.");
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            shoppingCartDao.clearCart(userId);
        }
        catch (Exception e)
        {
            throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error clearing shopping cart.");
        }
    }
}
