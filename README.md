# Chat-web-application

---

## Description
This is a full-stack e-commerce application built using Spring Boot for the backend and JavaScript for the frontend. The application allows users to have a one-to-one chat with one another.

---

## Table of Contents
- [Features](#features)
- [Dependencies](#dependencies)
- [Database Design](#Database-Design)
- [Installation](#installation)
- [Usage](#usage)
- [API Documentation](#API-Documentation)
- [Deployment](#deployment)

---

## Features
- User login and logout
- one-to-one chatting

---

## Dependencies
💡 **TODO:** 
- [x] add the dependencies
- Java version 17
- Spring Boot Version 3.3.0
  - Spring Web
  - Spring Data MongoDB
  - WebSocket
  - Lombok

---

## Database Design
💡 **TODO:** 
- [ ] add database schema diagram
- [ ] update the below data to reflect the up-to-date datase design in the application

<details>
  <summary>
    the entity classes of the database this application are: 

    the entity classes that represent many tables (self-referencing relationships):  ... ("..." and "..." tables).
    the many-to-many relationship tables are: 
    the supporting/"weak"-entity tables are: 
    the enumerated helper classes: 
    the record classes: 
  </summary>

  - relationships:
    - ... table:
      - has one-to-many relationship with ... table
    - ... table:
      - has many-to-one relationship with ... table

  - association of the database tables with their functionsalities/features in the application:
    - product table:
      - to ...
    - user table:
      - for ...
      - for ...
</details>

---

## application architecture:

💡 **TODO:** 
- [ ] add application architecture schema diagram

---

## application structure:


### folder structure
💡 **TODO:** 
- [ ] add this section

### exception handling
💡 **TODO:** 
- [ ] add this section

---

## Installation


### Prerequisites
- Java 11+
- MySQL
- Maven
- Git


### Backend Setup
- clone the repository:

```bash
git clone git@github.com:ali8137/Ecommerce-web-application-backend.git
cd Ecommerce-web-application-backend
```

- configure environment variables:

the environment variables: MYSQL_DB_USERNAME and MYSQL_DB_PASSWORD

- install dependencies:

```bash
mvn clean install
```

or using "ctrl + shift + o" in Intellij IDEA

- run the application:

```bash
mvn spring-boot:run
```


### Database Setup
- create the database:

```bash
mongodb -u root -p -e "CREATE DATABASE chat_app;"
```

or using MySQL workbench UI

- run the docker containers in the docker compose file

---

## Usage
- once the backend is running, you can access the app at http://localhost:8088

💡 **TODO:** 
- [ ] continue/fill the below section
### API Endpoints
- `GET /api/...` - ...
- `POST /api/...` - ...
- `PUT /api/...` - ...
- 

### Authentication

### Example Request

---

## API Documentation
- API Base URL: http://localhost:8088

💡 **TODO:** 
- [ ] add postman tests
- [ ] add Swagger API documentation

---

## Deployment

💡 **TODO:** 
- [ ] update this section
