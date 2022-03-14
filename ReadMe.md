# Recipe system
Service that implements the backend side of a simple recipe system.

The application is an API consisting on four endpoints that perform CRUD operations on a database that contains information 
about a list of recipes. Recipes hold information about the recipe name, number of servings, list of ingredients, cooking 
instructions, date and time of creation, and whether the dish is vegetarian or not.

The product is build on Java, using the Spring Boot framework. CRUD operations are persisted into a MySQL database that 
has been containerized using Docker. A SQL file stoed under src/main/resources/sql directory creates the necessary tables
for the database schema is initialized the first time the docker container is initialized.

The application has a wide test coverage. Both unit and automation tests have been used to ensure the correct behaviour of
the different functionalities of the application.

The application is protected against unauthorized user requests. For simplicity, in-memory authentication is used to store
user details.

The application contains API documentation on the following endpoints
- host:port/recipe-system/swagger-ui/index.html#/
- host:port/recipe-system/api-documentation

### Prerequisites
Prerequisites to install

* Java 17
* Gradle 7.4
* Postman
* Docker Desktop

### Build
Step-by-step build instructions.

1. Clone the repository into your local machine 
2. cd into the root directory of the project: ~/../recipe-system
3. docker-compose up
4. gradle clean build
5. gradle bootRun

### Functional testing

A postman collection called recipe-endpoints has been included in the root directory of the project. This collection can be imported into
Postman, and it contains different endpoints that can be used as base to test the different functionalities of the
application.