
# Ticketing System

Introduction
The Ticketing System is a Java and Angular-based application for managing ticket operations. It provides real-time 
ticket updates using WebSocket and a MongoDB backend for data persistence. The system features configurations for 
ticket release and retrieval rates, a synchronized ticket pool, and logs of all operations. Vendors can add tickets, 
and customers can purchase them in a user-friendly interface.

Setup Instructions

Prerequisites
1. Backend Requirements:
   - Java 17 or higher
   - Maven
   - MongoDB (running locally or hosted)
   - Spring Boot framework
2. Frontend Requirements:
   - Node.js (version 14 or higher)
   - Angular CLI (latest version)
3. Optional Tools:
   - Postman (for API testing)
   - MongoDB Compass (GUI for MongoDB)

Backend Setup
1. Clone the repository:
   git clone <repository-url>
   cd ticketing-system-backend
2. Configure MongoDB:
   Edit `application.properties` or `application.yml` with your MongoDB URI and database name:
   spring.data.mongodb.uri=mongodb://localhost:27017
   spring.data.mongodb.database=ticketing-system
3. Build and run the backend application:
   mvn clean install
   mvn spring-boot:run
   The backend will start at `http://localhost:8080`.

Frontend Setup
1. Navigate to the frontend directory:
   cd ticketing-system-frontend
2. Install dependencies:
   npm install
3. Start the development server:
   ng serve
   The frontend will be available at `http://localhost:4200`.

Usage Instructions

1. Configuring the System
Navigate to the Configuration Form in the UI. Set the desired ticket release rate, customer retrieval rate, and 
maximum ticket capacity. Click Save to apply the changes.

2. Starting the System
Use the Control Panel to:
- Start/stop vendors (to add tickets to the pool).
- Start/stop customers (to retrieve tickets from the pool).

3. Monitoring the System
Ticket Display: Shows the current number of available tickets and purchased tickets.
Log Display: Displays real-time logs for ticket operations and system activities.