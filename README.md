## API Endpoints
Swagger UI [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **POST** `/api/v1/users`: Create a new user.
- **GET** `/api/v1/users`: Retrieve all users.
- **DELETE** `/api/v1/users/{userId}`: Delete a user by ID.
- **PATCH** `/api/v1/users/{userId}`: Update user's phone number.
- **PUT** `/api/v1/users/{userId}`: Update user's information.
- **GET** `/api/v1/users/search/`: Search users by birth date range.

# TestRestApi

TestRestApi is a RESTful API project built using Spring Boot framework. It provides endpoints to perform CRUD operations on user data.

## Features

- **Create User**: Endpoint to create a new user with validation for email, first name, last name, and birth date.
- **Get All Users**: Endpoint to retrieve all users with pagination support.
- **Delete User**: Endpoint to delete a user by their ID.
- **Update Phone Number**: Endpoint to update a user's phone number.
- **Update User Information**: Endpoint to update a user's information including email, birth date, address, and phone number.
- **Search Users by Birth Date Range**: Endpoint to search users by specifying a birth date range.

## Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Lombok
- ModelMapper
- Liquibase
- Docker
- Maven

## Setup Instructions

1. Clone the repository:

```
git clone <repository-url>
```

2. Navigate to the project directory:

```
cd TestRestApi
```

3. Build the project using Maven:

```
mvn clean install
```

4. Run the application:

```
mvn spring-boot:run
```

5. Access the API endpoints using a tool like Postman or cURL.

## Configuration

- PostgreSQL database configuration can be found in the `application.properties` file.
- Age requirement for users is configured using the `user.min-age` property.
- Liquibase is used for database schema management. Change log files are located in the `db/changelog` directory.
- Docker configuration can be found in the `docker-compose.yml` file.




