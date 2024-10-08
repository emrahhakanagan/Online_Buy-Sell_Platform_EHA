<img src="images/logo.jpg" alt="logo" style="width:150px;height:auto;">

# Online Buy-Sell Platform EHA
#### in progress ...

Online Buy-Sell Platform EHA is a web application designed for buying and selling products online. This platform offers user authentication via email, the ability to add products for sale, and facilitates the purchase of products. The project is built using Spring Boot and integrates several key functionalities to ensure a smooth user experience.

## Project Description

Online Buy-Sell Platform EHA is an online marketplace where users can register, list items for sale, and purchase items from other users. The platform supports:

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
- **JUnit 5**: Used for unit testing the application.
- **Mockito**: Framework for creating mocks in unit tests.
- **MockMvc**: Used for testing Spring MVC controllers.
- **JaCoCo**: Java code coverage tool used to measure the percentage of code executed during tests.

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

## Testing

### Running Tests

To run unit and integration tests:

```sh
mvn test
```

### Test Coverage
To generate a test coverage report using JaCoCo:

```sh
mvn jacoco:report
```
The report can be found at target/site/jacoco/index.html.

## Continuous Integration
For CI/CD, you can configure GitHub Actions or another CI tool to automatically run tests and generate reports on every commit.

## Contributing

Contributions are welcome! If you have any suggestions or improvements, please fork the repository and create a pull request. 
For major changes, please open an issue first to discuss what you would like to change.


## Contact

Emrah Hakan AGAN - [emrahhakanagan@gmail.com](mailto:emrahhakanagan@gmail.com)

Project Link: [Online Buy-Sell Platform EHA](https://github.com/emrahhakanagan/Online_Buy-Sell_Platform_EHA)
