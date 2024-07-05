# Online BuySell Platform EHA

Online BuySell Platform EHA is a web application designed for buying and selling products online. This platform offers user authentication via email, the ability to add products for sale, and facilitates the purchase of products. The project is built using Spring Boot and integrates several key functionalities to ensure a smooth user experience.

## Project Description

Online BuySell Platform EHA  is an online marketplace where users can register, list items for sale, and purchase items from other users. The platform supports:

- **User Authentication**: Users can register and log in using their email addresses. Email verification is implemented for security.
- **Product Management**: Users can add, edit, and delete their products. Each product can include images, descriptions, prices, and categories.
- **Transaction Processing**: Users can purchase products listed by others, with the platform handling order management and notifications.
- **Secure Transactions**: Security measures include user authentication and data encryption to protect user information.

## Technologies Used

- **Java 17**: The core programming language used for development.
- **Spring Boot 3.3.1**: Framework used for building the application.
- **Spring Data JPA**: Used for database interactions.
- **Spring Security**: Implements security features including authentication and authorization.
- **Spring Boot Starter Mail**: Used for sending emails, such as for user registration and order notifications.
- **Freemarker**: Template engine for generating email content and rendering views.
- **PostgreSQL**: Database for storing user and product information.
- **Maven**: Build automation tool.

## Getting Started

To get a local copy of the project up and running, follow these steps.

### Prerequisites

Ensure you have the following installed on your local machine:

- Java 17
- Maven
- PostgreSQL

### Installation

1. **Clone the repository**:
    ```sh
    git clone https://github.com/yourusername/onlinebuysellplatform.git
    ```
2. **Navigate to the project directory**:
    ```sh
    cd onlinebuysellplatform
    ```
3. **Update the PostgreSQL configuration** in `application.properties`:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/yourdbname
    spring.datasource.username=yourusername
    spring.datasource.password=yourpassword
    ```
4. **Run the application**:
    ```sh
    mvn spring-boot:run
    ```

## Usage

- **Register**: Create a new user account by providing your email and setting up a password.
- **Login**: Authenticate with your email and password.
- **Add Product**: List a new product for sale by providing necessary details such as name, description, price, and category.
- **Browse Products**: View all products listed on the platform and search for items of interest.
- **Purchase Product**: Buy a product listed by another user and receive email notifications for order status.

## Contributing

Contributions are welcome! If you have any suggestions or improvements, please fork the repository and create a pull request. For major changes, please open an issue first to discuss what you would like to change.

## License

Distributed under the MIT License. See `LICENSE` for more information.

## Contact

Your Name - [your.email@example.com](mailto:your.email@example.com)

Project Link: [https://github.com/yourusername/onlinebuysellplatform](https://github.com/yourusername/onlinebuysellplatform)
