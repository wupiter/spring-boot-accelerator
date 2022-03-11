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

{{#if (eval authentication '==' 'keycloak-jwt')}}
## Authentication
The app is using [Keycloak](https://www.keycloak.org/), an open-source Identity and Access Management service for JWT based authentication.
Running the `docker-compose up` command as described above will also run Keycloak in a docker container (called `keycloak`).

You can access the admin console at http://localhost:8403/auth.

The docker container is pre-configured with the admin user with `admin/admin` credentials, and it also has a `demo_realm` realm created with a `users` group and a `user01/pwd123`
user. The realm is imported from the `_devops/docker/keycloak/demo_realm.json` config file.

For further docker configuration for Keycloak, visit https://www.keycloak.org/getting-started/getting-started-docker.

The realm has a client id `demo-app` configured with a user `user01/pwd123`.

The app uses Spring Security and Spring Security OAuth2 Resource Server libraries for authentication / authorization. See the `SecurityConfig` and `MethodSecurityConfig` classes and `application.yml` file (config properties under `spring.security` and `app.jwt`) for more details on the configuration.

Finally, the `TodoController` class is configured to require 'user' role for all endpoints declared within the controller and `TodoControllerTest` class is updated to support role-based authorization. 

{{/if}}
## Spring Data JPA
The application communicates via the SQL database using [Spring Data JPA](https://spring.io/projects/spring-data-jpa).

Use the `JPA Buddy` IntelliJ plugin to generate liquibase change-sets from JPA entities

# Lombok
The project uses [Lombok](https://projectlombok.org/) that auto-generates bytecode for getters, setters, logger, etc. 

To use Lombok in IntelliJ install the `Lombok` plugin and enable Settings -> Enable annotation processing. 

See below links for more details:
= https://www.baeldung.com/intro-to-project-lombok
- https://www.baeldung.com/lombok-configuration-system

# Error handling

The app has its own exception `ApiException`, which is handled by and translated to an error response (among with other common Spring exceptions) by the `GlobalErrorHandler` class.

# Automated tests
Automated tests are located under `<PROJECT_HOME>/src/test/java` with unit tests covering the REST, the service, and the repository layers.

# Java Bean Mapping using MapStruct
[MapStruct](https://mapstruct.org/) makes it easy to convert between different java beans (ie: entities and other, simple POJOs).

For more details, see basic usage in the `TodoMapper` interface.

# Code Quality checks
See here for more details: https://developer.okta.com/blog/2019/12/20/five-tools-improve-java.

{{#if (eval 'checkstyle' 'in' features)}}
## Checkstyle
[Checkstyle](https://checkstyle.org/) is an open-source tool available as both Gradle and Maven plugins. It can validate if the code is adhering to our coding standards.

A custom checkstyle configuration file is available at `demo-api/checkstyle.xml`.
{{#if (eval buildEngine '==' 'gradle')}}

Run `./gradlew checkstyleMain` in the `demo-api` folder to generate checkstyle report under `demo-api/build/reports/checkstyle`.
{{/if}}
{{#if (eval buildEngine '==' 'mvn')}}
Run `./mvnw checkstyle:checkstyle` in the `demo-api` folder to generate the checkstyle report at `demo-api/target/site/checkstyle.html`.
{{/if}}

For more details on configuring Checkstyle, see https://www.vogella.com/tutorials/Checkstyle/article.html.
{{/if}}
{{#if (eval 'jacoco' 'in' features)}}

## JaCoCo
[JaCoCo](https://www.eclemma.org/jacoco/) is an open-source code coverage tool available as both Gradle and Maven plugins.

{{#if (eval buildEngine '==' 'gradle')}}
Run `./gradlew test jacocoTestReport` in the `demo-api` folder to run unit tests and generate the code coverage report under `demo-api/build/reports/jacoco`. 
{{/if}}
{{#if (eval buildEngine '==' 'mvn')}}
Run `./mvnw test jacoco:report` in the `demo-api` folder to run unit tests and generate the code coverage report under `demo-api/target/site/jacoco`.
{{/if}}

For more details on JaCoCo, see below links:
* https://www.baeldung.com/jacoco
* https://docs.gradle.org/current/userguide/jacoco_plugin.html
{{/if}}
{{#if (eval 'owasp' 'in' features)}}

## OWASP vulnerability
[OWASP Dependency Check](https://owasp.org/www-project-dependency-check/) is an open-source Software Composition Analysis (SCA) tool that attempts to detect publicly disclosed vulnerabilities contained within a project’s dependencies.

{{#if (eval buildEngine '==' 'gradle')}}
Run `./gradlew dependencyCheckAnalyze` in the `demo-api` folder to generate the vulnerability report at `demo-api/build/reports/dependency-check-report.html`.
{{/if}}
{{#if (eval buildEngine '==' 'mvn')}}
Run `./mvnw dependency-check:check` in the `demo-api` folder to generate the vulnerability report at `demo-api/target/dependency-check-report.html`.
{{/if}}

For more details on OWASP Dependency Check, see below links:
* https://plugins.gradle.org/plugin/org.owasp.dependencycheck
* https://jeremylong.github.io/DependencyCheck/dependency-check-maven/
{{/if}}
{{#if (eval 'spotbugs' 'in' features)}}

## Spotbugs
[Spotbugs](https://spotbugs.github.io/) is an open-source program which uses static analysis to look for bugs in the compiled Java byte code.

{{#if (eval buildEngine '==' 'gradle')}}
Run `./gradlew spotbugsMain` in the `demo-api` folder to generate the bugs report in the console output.
{{/if}}
{{#if (eval buildEngine '==' 'mvn')}}
Run `./mvnw spotbugs:check` in the `demo-api` folder to generate the bugs report in the console output.
{{/if}}

For more details on Spotbugs, see below links:
* https://spotbugs.readthedocs.io/en/latest/gradle.html
* https://spotbugs.readthedocs.io/en/latest/maven.html
{{/if}}
{{#if (eval 'pmd' 'in' features)}}

## PMD
[PMD](https://pmd.github.io/) is an open-source program which uses static analysis to look for bugs in Java source code.

{{#if (eval buildEngine '==' 'gradle')}}
Run `./gradlew pmdMain` in the `demo-api` folder to generate the bugs report at `demo-api/build/reports/pmd/main.html`.
{{/if}}
{{#if (eval buildEngine '==' 'mvn')}}
Run `./mvnw spotbugs:check` in the `demo-api` folder to generate the bugs report at `demo-api/target/site/pmd.html`.
{{/if}}

For more details on Spotbugs, see below links:
* https://docs.gradle.org/current/userguide/pmd_plugin.html
* https://jeremylong.github.io/DependencyCheck/dependency-check-maven/
{{/if}}
{{#if (eval 'japicmp' 'in' features)}}

## JApiCmp
[JApiCmp](https://siom79.github.io/japicmp/) is an open-source tool to compare two versions of a jar archive.

{{#if (eval buildEngine '==' 'gradle')}}
Run `./gradlew pmdMain` in the `demo-api` folder to generate the bugs report at `demo-api/build/reports/pmd/main.html`.
{{/if}}
{{#if (eval buildEngine '==' 'mvn')}}
Run `./mvnw spotbugs:check` in the `demo-api` folder to generate the bugs report at `demo-api/target/site/pmd.html`.
{{/if}}

For more details on Spotbugs, see below links:
* https://github.com/melix/japicmp-gradle-plugin
* https://siom79.github.io/japicmp/MavenPlugin.html
{{/if}}
