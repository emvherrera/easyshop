# EasyShop E-Commerce API - Version 2

This project is the backend (server-side) for EasyShop, an online store. It's built with **Java Development** and **Spring Boot**, using a **MySQL Database** for data storage.We use **Postman** for API testing and **GitHub** for version control.

As a backend developer for Version 2, my main tasks include:

* **Fixing Bugs**: The existing API code is functional but contains several bugs. Specifically, I'll be addressing issues with the product search functionality returning incorrect results and products appearing duplicated in the database instead of being updated. I will use manual debugging and write unit tests to help find and fix these bugs.
* **Developing New Features**: I will be developing several new features for EasyShop.
    * **Categories Controller**: Implementing methods to manage product categories. This includes creating functionality for administrators to insert, update, and delete categories.Only users with the `ADMIN` role will be allowed to perform these actions.
     
    * **Shopping Cart**: This is a new feature that allows logged-in users to add items to their shopping cart, view their cart contents, update product quantities within the cart, and clear their cart.The shopping cart will persist even if the user logs out.
    * **User Profile**: Users will have the ability to view and update their personal profiles. A `ProfileController` will be created, and the existing `ProfileDao` will be updated to include `getByUserId` and `update` methods.
  * **Checkout**: This significant new feature involves converting a user's shopping cart into an order.An `OrdersController` will be created to retrieve the current user's shopping cart, create and insert a new order into the `orders` table, add each shopping cart item as an `OrderLineItem`, and then clear the shopping cart.

The application has existing functionalities like user registration and login, displaying products by category, and filtering products.The database includes sample products and three demo users (`user`, `admin`, and `george`), all with the password "password". The frontend website project is also available for testing the API's usage on the web.


![image](https://github.com/user-attachments/assets/8c32c84f-4e20-492c-b16a-4e4cdffe15b0)
![image](https://github.com/user-attachments/assets/4e370ea5-954a-46a4-9f5d-d78f6a4d1a19)
![image](https://github.com/user-attachments/assets/d1c29e18-926f-469e-8f7f-4184091f0e5a)
![image](https://github.com/user-attachments/assets/ddc8b4ba-79d4-48f2-97ed-e86dbcb9fbe0)
![image](https://github.com/user-attachments/assets/7227ff7f-d2f8-4155-adc6-55db9cdae32b)
![image](https://github.com/user-attachments/assets/61eef383-081b-4de3-aa92-7616cfe30ddb)
![image](https://github.com/user-attachments/assets/92a54fd1-2a14-462d-bd58-822bcb7f7b7b)
![image](https://github.com/user-attachments/assets/ce9af32a-0a23-4366-8f1c-d3ef19ea5d28)


