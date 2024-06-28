# EasyShop

## EasyShop is a web-based e-commerce application that provides a seamless shopping experience for users. It features user authentication, product management, category organization, and shopping cart functionality.

## Project Overview

This project was developed as part of a course assignment. The initial codebase and structure were provided by the
instructor. My primary contributions focused on implementing the shopping cart functionality and related features.

## Features

- User registration and authentication
- Product browsing and searching
- Category management
- Shopping cart operations
- Admin panel for product and category management

## Technologies Used

- Java JDK 17
- Spring Boot 3.1.0
- Spring Security 6.1.0
- MySQL 8.0
- Maven 3.8.4

## Setup

### Prerequisites

- Java JDK 17 or later
- Maven 3.8.4 or later
- MySQL 8.0 or later
- An IDE that supports Spring Boot development (e.g., IntelliJ IDEA, Eclipse, or VS Code)

### Database Setup

The EasyShop application uses a MySQL database with the following tables:

1. `users`: Stores user authentication information
2. `profiles`: Stores user profile information
3. `categories`: Stores product categories
4. `products`: Stores product information
5. `orders`: Stores order information
6. `order_line_items`: Stores individual items within an order
7. `shopping_cart`: Stores items in a user's shopping cart

The SQL script to create the database and tables is located in the `database` package in IntelliJ, in a file
named `create_database.sql`.

You can set up the database in one of two ways:

1. **Copy the SQL Script**:
   You can copy the entire contents of the `create_database.sql` file and run it in your MySQL client or workbench.

2. **Direct Download**:
   For your convenience, you can download the SQL script directly by
   clicking [here](https://github.com/izabekovaisha/EasyShop/blob/master/database/create_database.sql).

Once you have the SQL script, follow these steps:

1. Open your MySQL client or workbench.
2. Connect to your MySQL server.
3. Run the SQL script to create the `easyshop` database and all necessary tables.

This script will:

- Create the `easyshop` database if it doesn't exist
- Create tables for users, profiles, categories, products, orders, order line items, and shopping cart
- Insert initial data, including some default users

After running the script, your database will be set up and ready for the application to use.

## Running the Application

1. Clone the repository to your local machine using Git:
   `git clone https://github.com/yourusername/easyshop.git`
2. Open the project in your preferred IDE.
3. Set up the database using the provided SQL script in `database/create_database.sql`.
4. Update the `application.properties` file with your MySQL database credentials.
   `spring.datasource.url=jdbc:mysql://localhost:3306/easyshop
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password`
5. Build the project using Maven
6. Run the application
7. The application will be available at `http://localhost:8080`.

## Running the Web Application

To run the EasyShop web application, follow these steps:

1. Start the backend server:

* Navigate to the capstone-starter directory
* Run the Spring Boot application (this will start the backend server)

2. Access the application:

* Open your preferred web browser
* Navigate to the capstone-client-web-application directory
* Open the index.html file directly in your browser

You should now see the EasyShop web application running in your browser. You can interact with the application, browse
products, add items to your cart, and perform other actions as implemented.

## Application Demo

Here's a GIF demonstration of the EasyShop web application in action:

![ezgif com-optimize](https://github.com/izabekovaisha/EasyShop/assets/166551874/cd0cc395-66db-45e7-8856-fd147fae68bf)

## API Endpoints

**User Authentication**

* POST /auth/register - Register a new user
* POST /auth/login - Login a user

**Products**

* GET /products - Get all products
* GET /products/{id} - Get a specific product
* POST /products - Add a new product (Admin only)
* PUT /products/{id} - Update a product (Admin only)
* DELETE /products/{id} - Delete a product (Admin only)

**Categories**

* GET /categories - Get all categories
* GET /categories/{id} - Get a specific category
* GET /categories/{id}/products - Get all products in a category
* POST /categories - Add a new category (Admin only)
* PUT /categories/{id} - Update a category (Admin only)
* DELETE /categories/{id} - Delete a category (Admin only)

**Shopping Cart**

* GET /cart - Get the current user's shopping cart
* POST /cart/products/{id} - Add a product to the cart
* PUT /cart/products/{id} - Update the quantity of a product in the cart
* DELETE /cart/products/{id} - Remove a product from the cart
* DELETE /cart - Clear the entire cart

## API Testing with Postman

To test the EasyShop API endpoints, you can use Postman. Here are some example requests:

1. Get all products (no authorization required):
    - Method: GET
    - URL: `http://localhost:8080/products`

2. Get a specific product (no authorization required):
    - Method: GET
    - URL: `http://localhost:8080/products/{id}`

3. Add a new product (Admin authorization required):
    - Method: POST
    - URL: `http://localhost:8080/products`
    - Headers:
        - Authorization: Bearer <admin_jwt_token>
    - Body (raw JSON):
      ```json
      {
        "name": "New Product",
        "price": 19.99,
        "categoryId": 1,
        "description": "A new product description"
      }
      ```

4. Get the current user's shopping cart:
    - Method: GET
    - URL: `http://localhost:8080/cart`
    - Headers:
        - Authorization: Bearer <user_jwt_token>

5. Add a product to the cart:
    - Method: POST
    - URL: `http://localhost:8080/cart/products/{productId}`
    - Headers:
        - Authorization: Bearer <user_jwt_token>

6. Update product quantity in the cart:
    - Method: PUT
    - URL: `http://localhost:8080/cart/products/{productId}`
    - Headers:
        - Authorization: Bearer <user_jwt_token>
    - Body (raw JSON):
      ```json
      {
        "quantity": 2
      }
      ```

7. Remove a product from the cart:
    - Method: DELETE
    - URL: `http://localhost:8080/cart/products/{productId}`
    - Headers:
        - Authorization: Bearer <user_jwt_token>

8. Clear the entire cart:
    - Method: DELETE
    - URL: `http://localhost:8080/cart`
    - Headers:
        - Authorization: Bearer <user_jwt_token>

Remember to replace `<admin_jwt_token>` with an actual JWT token for an admin user, and `<user_jwt_token>` with a JWT
token for a regular user. You'll need to obtain these tokens by logging in with the appropriate credentials.

### Postman Test Results

Here's a GIF demonstration of all passed requests in Postman, showing that the API endpoints are working correctly:

![ScreenRecording2024-06-27154317-ezgif com-video-to-gif-converter](https://github.com/izabekovaisha/EasyShop/assets/166551874/32d78cbb-3daa-4fae-9f9c-1972a187b465)

**Note:** The Postman collection 'easyshop-optional-solo' includes some tests that fail. These failures are related to
the user profile and checkout phases, which were optional features not implemented in this version of the application.
All other functionality, including the shopping cart operations, work properly as demonstrated in the GIF above.

## Project Structure

**Models**

* User: Represents a user in the system.
* Product: Represents a product available for purchase.
* Category: Represents a category of products.
* ShoppingCart: Represents a user's shopping cart.
* ShoppingCartItem: Represents an item in the shopping cart.

**Controllers**

* AuthenticationController: Handles user registration and login.
* ProductsController: Manages product-related operations.
* CategoriesController: Manages category-related operations.
* ShoppingCartController: Manages shopping cart operations.

**Data Access Objects (DAOs)**

* UserDao: Interface for user-related database operations.
* ProductDao: Interface for product-related database operations.
* CategoryDao: Interface for category-related database operations.
* ShoppingCartDao: Interface for shopping cart-related database operations.

**MySQL Implementations of DAOs**

* MySqlUserDao: MySQL implementation of UserDao.
* MySqlProductDao: MySQL implementation of ProductDao.
* MySqlCategoryDao: MySQL implementation of CategoryDao.
* MySqlShoppingCartDao: MySQL implementation of ShoppingCartDao.

## Spring Boot Application

The EasyShop application is created using the @SpringBootApplication annotation, which serves as the entry point for the
Spring Boot application. This annotation enables auto-configuration, component scanning, and configuration properties
support.

## Future Work

EasyShop is continually evolving. Planned enhancements include:

* Implementing a recommendation system based on user browsing history
* Adding a review and rating system for products
* Integrating a payment gateway for real transactions
* Developing a mobile application for enhanced accessibility

## Resources

- [Stack Overflow](https://stackoverflow.com/)
- [Java Code Geeks](https://www.javacodegeeks.com/)
- [DZone Java Zone](https://dzone.com/java-jdk-development-tutorials-tools-news)

## User Stories

Click [here](https://github.com/users/izabekovaisha/projects/11/views/1) to view the guiding user stories for this
project.

## Conclusion

This `README.md` file provides a comprehensive overview of the EasyShop application, including setup instructions,
database schema, API endpoints, Postman examples for testing the API, and a detailed project structure. If you have any
additional information or specific details you'd like to include, please let me know!





