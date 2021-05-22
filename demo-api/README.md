#Overview
This is a simple micro-service for creating and managing Todos using
[Java](https://www.java.com/), [Spring Boot](https://spring.io/projects/spring-boot) and [Docker](https://www.docker.com/).

# Database

## Start DB in docker container
Make sure to have `docker` and `docker-compose` installed first.
Run below commands:
```
cd <PROJECT_HOME>/_devops/docker
docker-compose up
```

The database connection details are found in `<PROJECT_HOME>/src/main/resources/application.yml`.

## Database versioning
This project uses [Liquibase](https://www.liquibase.org/) to version the database changes, see here for the liquibase change-sets config file: `<PROJECT_HOME>/src/main/resources/db.changelog/changelog-master.xml`.

## Spring Data JPA
The application communicates via the SQL database using [Spring Data JPA](https://spring.io/projects/spring-data-jpa).

Use the `JPA Buddy` IntelliJ plugin to generate liquibase change-sets from JPA entities

# Lombok
The project uses [Lombok](https://projectlombok.org/) that auto-generates bytecode for getters, setters, logger, etc. 

To use Lombok in IntelliJ install the `Lombok` plugin and enable Settings -> Enable annotation processing. 

# Error handling

The app has its own exception `ApiException`, which is handled by and translated to an error response (among with other common Spring exceptions) by the `GlobalErrorHandler` class.

# Automated tests
Automated tests are located under `<PROJECT_HOME>/src/test/java` with unit tests covering the REST, the service, and the repository layers.

# Java Bean Mapping using MapStruct
[MapStruct](https://mapstruct.org/) makes it easy to convert between different java beans (ie: entities and other, simple POJOs).

For more details, see basic usage in the `TodoMapper` interface.