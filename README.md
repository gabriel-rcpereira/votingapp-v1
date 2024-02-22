# VoteApp #2 Round
Take a look at the main [README](https://github.com/gabriel-rcpereira/votingapp-v1).

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

# VoteApp #3 Round
Take a look at the main [README](https://github.com/gabriel-rcpereira/votingapp-v1).

## Requirements

- [ ] Provision the Docker Container Postgres
- [ ] Create the database tables with Flyway/Liquibase
- [ ] Create the service image, docker compose, etc. Set the same settings for the resource as the backend contest(rinha de backend)
- [ ] Create the Performance Tests

## References
- ?