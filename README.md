# Ticker Desk

Ticker Desk is a full-featured IT helpdesk ticketing system designed to streamline and manage support requests efficiently. Built with Spring Boot, it provides a robust backend with features like user authentication, role-based access control, ticket management, and more.

## Features

*   **User Management:**
    *   User registration and login with JWT-based authentication.
    *   Admin and Manager roles for user management.
    *   Activate/deactivate user accounts.
    *   User profile management.
*   **Ticket Management:**
    *   Create, update, and delete tickets.
    *   Assign tickets to technicians.
    *   Track ticket status (Open, In Progress, Resolved, Closed).
    *   Set ticket priority (Low, Medium, High, Urgent).
    *   Categorize tickets (e.g., Hardware, Software, Network).
    *   Add comments and attachments to tickets.
*   **API Endpoints:**
    *   RESTful API for all major functionalities.
    *   Secure endpoints with role-based access control.
    *   API documentation can be generated using tools like Swagger.
*   **Service Catalog:**
    *   Define and manage a catalog of services.
    *   Associate tickets with service catalog items.

## Technologies Used

*   **Backend:**
    *   Java 21
    *   Spring Boot 3
    *   Spring Security (for authentication and authorization)
    *   Spring Data JPA (for database interaction)
    *   MySQL (as the database)
    *   Lombok (to reduce boilerplate code)
    *   JJWT (for JSON Web Token support)
*   **Build Tool:**
    *   Maven

## Getting Started

### Prerequisites

*   Java 21 or higher
*   Maven
*   MySQL

### Installation

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/ticker-desk.git
    cd ticker-desk
    ```
2.  **Configure the database:**
    *   Open `src/main/resources/application.properties`.
    *   Update the `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password` properties with your MySQL database details.
3.  **Build the project:**
    ```bash
    mvn clean install
    ```
4.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```
    The application will start on `http://localhost:8080`.

## API Endpoints

The following are the main API endpoints provided by the application.

### Authentication

*   `POST /api/auth/login`: Authenticate a user and receive a JWT token.
*   `POST /api/auth/register`: Register a new user.
*   `POST /api/auth/refresh`: Refresh an expired JWT token.
*   `POST /api/auth/logout`: Log out the current user.
*   `POST /api/auth/validate-token`: Validate a JWT token.
*   `GET /api/auth/me`: Get the current user's information.
*   `POST /api/auth/forgot-password`: Request a password reset.
*   `POST /api/auth/reset-password`: Reset the user's password.

### Users

*   `POST /api/users`: Create a new user (Admin/Manager only).
*   `GET /api/users/{id}`: Get a user by their ID.
*   `PUT /api/users/{id}`: Update a user's information.
*   `DELETE /api/users/{id}`: Delete a user (Admin only).
*   `GET /api/users`: Get a paginated list of all users (Admin/Manager only).
*   `GET /api/users/search`: Search for users by name.
*   `GET /api/users/department/{department}`: Get users by department.
*   `GET /api/users/site/{site}`: Get users by site.
*   `GET /api/users/role/{role}`: Get users by role.
*   `GET /api/users/active`: Get all active users.
*   `GET /api/users/inactive`: Get all inactive users.
*   `PUT /api/users/{id}/activate`: Activate a user account (Admin only).
*   `PUT /api/users/{id}/deactivate`: Deactivate a user account (Admin only).
*   `PUT /api/users/{id}/change-password`: Change a user's password.
*   `GET /api/users/profile`: Get the current user's profile.
*   `PUT /api/users/profile`: Update the current user's profile.
*   `GET /api/users/statistics`: Get user statistics (Admin/Manager only).
*   `GET /api/users/technicians/{department}`: Get active technicians by department.
*   `POST /api/users/bulk`: Create multiple users in bulk (Admin only).
*   `GET /api/users/check-email`: Check if an email address is already in use.
*   `GET /api/users/check-employee-id`: Check if an employee ID is already in use.

## Contributing

Contributions are welcome! Please feel free to submit a pull request.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
