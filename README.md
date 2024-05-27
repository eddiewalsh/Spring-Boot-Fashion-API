# 🌟 Zelora Fashion Store API & Client Application
## 📚 Table of Contents
* Overview
* Features
* Technologies Used
* API Endpoints
* Known Issues
* Upcoming Improvements

### 📝 Overview
Zelora is a fashion store API developed using Spring Boot, Spring Security, and Java. The API provides QR codes for different sections of the application, following HATEOS principles, and secures endpoints using JWT. The client application is built with ASP .NET Razor pages and utilizes the Singleton pattern to manage the JWT token across the client.

### ✨ Features
#### 🌐 REST API:
* Provides QR codes for various sections.
* Follows HATEOS principles for self-descriptive API responses.
* Secured with JWT for authentication.

#### 🖥️ Client Application:
* Built with ASP .NET Razor pages.
* Implements Singleton pattern for efficient JWT token management.

#### 💻 Technologies Used
##### Backend
* Spring Boot: Framework for building microservices and web applications.
* Spring Boot Starter Data JPA: For database operations.
* Spring Boot Starter HATEOAS: Supports hypermedia-driven RESTful APIs.
* Spring Boot Starter Security: Adds security features for authentication and authorization.
* Spring Boot Starter Web: Facilitates the creation of RESTful web services.
* Spring Boot DevTools: Enhances the development experience.
* Spring Security: Secures applications, handling authentication, and authorization.

* JWT (JSON Web Tokens): Securely transmits information between parties as a JSON object.

* io.jsonwebtoken (jjwt): Library for creating and verifying JWTs.
* Hibernate Validator: Data validation framework.

* Jackson: Processes JSON.

* Jackson Dataformat XML: Adds support for XML data formats.
* Lombok: Reduces boilerplate code by generating getters, setters, and other methods.

* JAXB (Java Architecture for XML Binding): Converts Java objects to XML and vice versa.

* jakarta.xml.bind-api and jaxb-runtime: Provide JAXB implementation.
* jakarta.activation-api: Supports data type definitions required by JAXB.
* PDF Libraries: For handling PDF generation and manipulation.

* iText: Library for creating and manipulating PDF documents.
* Apache PDFBox: Library for working with PDF documents.
* ZXing (Zebra Crossing): Generates QR codes.

* zxing core and zxing javase: Core libraries for generating and reading barcodes and QR codes.
##### Database
* MySQL: Relational database management system.

* MySQL Connector/J: JDBC driver for connecting to MySQL databases.
* MariaDB: Open-source relational database management system.

* MariaDB Java Client: JDBC driver for connecting to MariaDB databases.
##### Testing
* Spring Boot Starter Test: Provides testing support for Spring Boot applications.
##### Build Tool
* Maven: Manages project dependencies and builds the project.

#### 📊 API Endpoints
##### Category Endpoints
* GET /zelora/category/{id}: Retrieve a category by ID.
##### Inventory Endpoints
* GET /zelora/inventory/products/{productID}: Retrieve product inventory by product ID.
* GET /zelora/inventory/supplier/{id}: Retrieve supplier inventory by supplier ID.
* GET /zelora/inventory/{id}: Retrieve inventory by ID.
##### Order Item Endpoints
* GET /zelora/order/items/product/{id}: Retrieve order items by product ID.
* GET /zelora/order/items/{id}: Retrieve order items by ID.
##### Product Endpoints
* GET /zelora/products/: Retrieve all products.
* GET /zelora/products/category/{id}: Retrieve products by category ID.
* GET /zelora/products/supplier/{id}: Retrieve products by supplier ID.
* GET /zelora/products/{id}: Retrieve a product by ID.
* GET /zelora/products/{id}/large/image: Retrieve large image of a product by ID.
* GET /zelora/products/{id}/thumbs/image: Retrieve thumbnail image of a product by ID.
* GET /zelora/products/{productID}/qrcode: Retrieve QR code for a product by product ID.
##### Review Endpoints
* GET /zelora/reviews/: Retrieve all reviews.
* GET /zelora/reviews/customers/{id}: Retrieve reviews by customer ID.
* GET /zelora/reviews/products/{id}: Retrieve reviews by product ID.
* GET /zelora/reviews/{id}: Retrieve a review by ID.
##### Supplier Endpoints
* GET /zelora/supplier/{id}: Retrieve a supplier by ID.
##### Discount Endpoints
* GET /zelora/discounts/birthday/{email}: Retrieve birthday discount by email.
* GET /zelora/discounts/customers/{id}: Retrieve discounts by customer ID.
* GET /zelora/discounts/first/{email}: Retrieve first-time discount by email.
* GET /zelora/discounts/sustainable/{email}: Retrieve sustainable discount by email.
* GET /zelora/discounts/tenth/{email}: Retrieve tenth purchase discount by email.
* GET /zelora/discounts/{discountID}/qrcode: Retrieve QR code for a discount by discount ID.
* GET /zelora/discounts/{id}: Retrieve a discount by ID.
##### Authentication Endpoint
* POST /authenticate: Authenticate a user and return a JWT token.
##### Wishlist Endpoints
* POST /zelora/wishlist/add: Add an item to the wishlist.
* GET /zelora/wishlist/customers/{id}: Retrieve wishlist by customer ID.
* GET /zelora/wishlist/product/{id}: Retrieve wishlist by product ID.
* POST /zelora/wishlist/remove: Remove an item from the wishlist.
* POST /zelora/wishlist/update: Update the wishlist.
* GET /zelora/wishlist/{id}: Retrieve a wishlist by ID.
##### Customer Endpoints
* GET /zelora/customers/: Retrieve all customers.
* GET /zelora/customers/email/{email}: Retrieve a customer by email.
* POST /zelora/customers/register: Register a new customer.
* DELETE /zelora/customers/remove/{id}: Remove a customer by ID.
* POST /zelora/customers/update: Update a customer.
* GET /zelora/customers/{id}: Retrieve a customer by ID.
##### Order Endpoints
* GET /zelora/orders/: Retrieve all orders.
* POST /zelora/orders/create: Create a new order.
* GET /zelora/orders/customers/{customerID}: Retrieve orders by customer ID.
* GET /zelora/orders/{id}: Retrieve an order by ID.
* POST /zelora/orders/{id}/status/{newStatus}: Update the status of an order.
* GET /zelora/orders/{orderID}/items: Retrieve items in an order.
* GET /zelora/orders/{orderID}/pdf: Retrieve order details in PDF format.
* POST /zelora/orders/{orderID}/redeem/{discountCode}: Redeem a discount code for an order.

#### 🐞 Known Issues
* Some sections of the application may not work as expected.
* Limited comments in the code. More comments will be added soon.

#### 🔧 Upcoming Improvements
* Adding more detailed comments throughout the codebase.
* Fixing known issues and improving functionality.
* Enhancing documentation for better clarity and usability.
