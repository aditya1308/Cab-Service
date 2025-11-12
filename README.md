# Cab-Service

This repository contains the backend for a cab booking service, inspired by applications like Uber and Ola. It provides functionalities for user and admin management, trip booking, fare calculation, and OTP verification. The application is built using Java and the Spring Boot framework.

## Features

*   **User Management:**
    *   User registration and login.
    *   Secure password storage using BCrypt.
    *   Forgot password functionality with OTP verification.
*   **Admin Management:**
    *   Separate registration and login for administrators.
    *   Admin registration protected by a secret code.
*   **OTP Verification:**
    *   Sends and verifies OTPs for both email and mobile numbers.
    *   Integrates with Gmail SMTP for email and Fast2SMS for mobile OTPs.
*   **Trip Management:**
    *   Book a trip by providing an origin and destination.
    *   Automatically calculates trip distance using the Google Distance Matrix API.
    *   Calculates the fare based on the distance.
    *   Users can view their complete trip history.

## Technology Stack

*   **Backend:** Java 17, Spring Boot, Spring Data JPA, Spring Security
*   **Database:** MySQL
*   **Build Tool:** Maven
*   **Libraries:**
    *   ModelMapper: For object mapping.
    *   Lombok: To reduce boilerplate code.
    *   OkHttp: For making HTTP requests to external APIs.
    *   Twilio/Fast2SMS-compatible API: For sending SMS.
    *   JavaMail: For sending emails.

## Getting Started

### Prerequisites

*   Java Development Kit (JDK) 17 or later.
*   Apache Maven.
*   MySQL Server.
*   API keys for Google Distance Matrix and an SMS provider (like Fast2SMS).
*   A Gmail account with an "App Password" for sending emails.

### Installation & Setup

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/aditya1308/Cab-Service.git
    cd Cab-Service/Backend
    ```

2.  **Database Setup:**
    Create a new database in your MySQL instance.
    ```sql
    CREATE DATABASE cab_service_db;
    ```

3.  **Configuration:**
    Create an `application.properties` file in the `src/main/resources` directory and add the following configuration. Replace the placeholder values with your actual credentials and keys.

    ```properties
    # Database Configuration
    spring.datasource.url=jdbc:mysql://localhost:3306/cab_service_db
    spring.datasource.username=your_db_username
    spring.datasource.password=your_db_password
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true

    # Google Distance Matrix API Configuration
    api-key=YOUR_GOOGLE_API_KEY
    distance-matrix-api=https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s&destinations=%s&key=%s

    # Fast2SMS API Configuration for Mobile OTP
    fast2.sms.api-key=YOUR_FAST2SMS_API_KEY
    fast2.sms.url=https://www.fast2sms.com/dev/bulkV2
    fast2.sms.content-type=application/x-www-form-urlencoded
    ```
    **Note on Email OTP:** The email credentials are currently hardcoded in `src/main/java/com/service/cab/util/OTPGenerator.java`. For production use, it is highly recommended to externalize these to the `application.properties` file. You will need to replace `"service.cab.app@gmail.com"` and the app password `"qgrizchibivtlwpb"` with your own credentials.

4.  **Run the application:**
    Use the Maven wrapper to build and run the Spring Boot application.
    ```sh
    ./mvnw spring-boot:run
    ```
    The server will start on `http://localhost:8080`.

## API Endpoints

The following are the available API endpoints.

### User Endpoints

| Method | Endpoint                             | Description                                 |
| :----- | :----------------------------------- | :------------------------------------------ |
| `POST` | `/register`                          | Registers a new user.                       |
| `POST` | `/login`                             | Authenticates and logs in a user.           |
| `POST` | `/forgot-password`                   | Initiates the password reset process.       |
| `POST` | `/send-email-otp`                    | Sends an OTP to the user's email.           |
| `POST` | `/verify-email-otp`                  | Verifies the OTP sent to the email.         |
| `POST` | `/send-mobile-otp`                   | Sends an OTP to the user's mobile number.   |
| `POST` | `/verify-mobile-otp`                 | Verifies the OTP sent to the mobile number. |
| `POST` | `/{userId}/book-trip`                | Books a new trip for a specific user.       |
| `GET`  | `/{userId}/trips`                    | Fetches details of a user's trips.          |
| `GET`  | `/{userId}/trips-list`               | Retrieves a list of all trips for a user.   |

### Admin Endpoints

| Method | Endpoint                   | Description                                  |
| :----- | :------------------------- | :------------------------------------------- |
| `POST` | `/admin/register`          | Registers a new admin (requires `adminCode`). |
| `POST` | `/admin/login`             | Authenticates and logs in an admin.          |
| `POST` | `/admin/forgot-password`   | Initiates the admin password reset process.  |
