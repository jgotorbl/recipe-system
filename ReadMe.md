# Recipe system
Service that implements the backend side of a simple recipe system.

Its main functionalities are performing CRUD operations on recipes. Recipes hold information about the recipe name, 
number of servings, list of ingredients, cooking instructions, date and time of creation, and whether the dish is vegetarian 
or not.

The product is build on Java, using the Spring Boot framework. CRUD operations are persisted into a mysql database.


### Prerequisites
Prerequisites to install

* Java 17
* Gradle 7.4
* Postman
* Docker Desktop

### Build
Step-by-step build instructions.

1.  cd into the project directory ~/../recipe-system
2.  docker-compose up
3.  gradle clean build
4.  gradle bootRun

### Functional testing

A postman collection called recipe-endpoints has been included in the root directory of the project. This collection can be imported into
Postman, and it contains different endpoints that can be used as base to test the different functionalities of the
application.