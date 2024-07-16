# VoteApp
The application registers pools and users votes as well as returns the pools' result.

### Journey
This is a playground project that I will maintain in my spare time. I want to use it to create a journey to practice new approaches and perfect the known ones.

## Stack - Why did I choose such a tooling?
- [Java 17](https://jdk.java.net/17/) - The latest LTS version
- [Spring Boot 3.1.1](https://spring.io/projects/spring-boot) - The latest version
- [Spring Data JDBC](https://docs.spring.io/spring-data/jdbc/docs/current/reference/html/) - I get to test/master my knowledge related to it
- [H2 (Postgres compatibility)](https://www.h2database.com/html/main.html) - For now, it's enough
- [Lombok](https://projectlombok.org/) - I like working with such a tools as it helps to deal with Java verbosity
- [Clean Architecture(Pragmatic)](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) - I have worked with many approaches. IMO, this one has proved the most pragmatic and flexible
- [TSID](https://github.com/f4b6a3/tsid-creator) - [It's a UUID that works really well as primary key](https://vladmihalcea.com/uuid-database-primary-key/)

## References
- [Spring Data jdbc getting started](https://thorben-janssen.com/spring-data-jdbc-getting-started/#:~:text=Spring%20Data%20JDBC%20is%20an,of%20entity%20objects%20and%20caching)
- [The series of articles](https://spring.io/blog/2021/09/09/spring-data-jdbc-how-to-use-custom-id-generation)
- [Spring JDBC Documentation](https://docs.spring.io/spring-data/jdbc/docs/current/reference/html/)

# Phases

# #1 Stage - POC
The main goal while creating the first version is to have a [POC](https://en.wikipedia.org/wiki/Proof_of_concept) at the end. 
That's the opportunity to understand the domain, user behavior while using the application and what we can do better in the next phase. 
Here is the [V1](https://github.com/gabriel-rcpereira/votingapp-v1/tree/round-1).

At this moment, only the main needs were addressed, in other words, the Non-Functional Requirements and the Application Design were left aside. For now, only the feasibility matters.

I created a few Integrated Tests. The tests provide a quick way to validate everything at once as the application is still changing.

# #2 Stage

## Requirements
In addition to the requirements listed below, I'd like to give a chance to AI as a copilot ;) 

- [x] Remove Pool Options Gateway
- [x] Validate the domain and endpoint attributes according to their needs
- [x] Replace the current exceptions with customized ones
- [x] Handle the Pool and Pool Options according to the relation Aggregate and Aggregate Root
  - I dropped this requirement. How Spring Data JDBC handles the aggregate and root relationship doesn't address the application requirements. In short, the aggregated items are deleted and recreated whenever it changes. 
- [x] Implement the controller advice and define a well-structured error response.\
  - The error message must contain the error code and details.
  - The http status response should follow the RestFul best practices.
- [ ] Implement the unit tests
  - I postponed this task. I implemented some unit tests by using AWS Code Whisperer and I got surprised how it incredibly works well.\
  BTW, I'm still using the free version.
- [x] Refactor the Integration Tests

## References
- https://www.baeldung.com/javax-validation-method-constraints
- https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-builtin-method-constraints
- https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-validating-executable-constraints

# #3 Stage

## Requirements

- [ ] Provision Postgres with Docker
- [ ] Create database tables with Flyway/Liquibase
- [ ] Create service image, docker compose, etc. Set the same settings for the resource as the backend contest(rinha de backend)
- [x] Create Performance Tests

## References
- https://docs.gatling.io/
