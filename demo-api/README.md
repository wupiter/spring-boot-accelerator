# Database

Use JPA Buddy IntelliJ plugin to generate liquibase changeset from JPA entities

## Start DB in docker container
Make sure to have `docker` and `docker-compose` installed first.
RUn below commands:
```
cd <PROJECT_HOME>/_devops/docker
docker-compose up
```

# Lombok
The project uses [Lombok](https://projectlombok.org/) that auto-generates bytecode for getters, setters, logger, etc. 

To use Lombok in IntelliJ install the Lombok plugin and enable Settings -> Enable annotation processing. 