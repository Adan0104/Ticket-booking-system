# Ticket Booking System #

A learning project that simulates the core backend functionality of a train ticket booking system (similar to IRCTC).
This project is built entirely in Java and currently operates through a Command Line Interface (CLI). It serves as a foundation for understanding backend design concepts such as data persistence, serialization, and user management.

🚀 Features
1. User Registration - Create a new account securely using hashed passwords.
2. User Login - Authenticate existing users with password verification.
3. Fetch My Bookings - View all tickets booked by the logged-in user.
4. Search Trains - Find available trains between stations.
5. Book a Seat - Reserve a specific seat on a selected train.
6. Cancel Booking - Cancel an existing ticket and free up the seat.

🛠️ Tech Stack
1. Java – Core programming language.
2. Jackson – For serialization and deserialization of local JSON-based databases (users.json, trains.json).
3. BCrypt – For secure password hashing and verification.
