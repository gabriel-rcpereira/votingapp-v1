# VoteApp
The application registers pools and users votes as well as returning the pools' result.

## Requirements

### FR1 - Create new Pool and its options

As a user, I want to create a new pool and provide what are the options available for voting

**Current state**

I'm not able to create a new pool

**Desired state**

I can create a new pool and provide its attributes:
- pool name
- expiration date
- unlimited pool options, each pool option has a name

### FR2 - Register users votes

As a user, I want to participate in any non-expired pool by voting in my favorite pool option

**Current state**

I'm not able to participate in any pool

**Desired state**

I can participate in any non-expired pool. I can vote in my favorite pool option.

### FR3 - Return the pool result

As a user, I want to consult the pool result aggregated by pool options and their corresponding percentages

**Current state**
I can't consult the pool result

**Desired state**
I can consult any pool result aggregated by pool options and their corresponding percentages

## Stack
- Java 17
- Spring Boot 3.1.1
- Spring Data JDBC
- H2 (Postgres compatibility)
- Lombok

## References
- https://thorben-janssen.com/spring-data-jdbc-getting-started/#:~:text=Spring%20Data%20JDBC%20is%20an,of%20entity%20objects%20and%20caching
- https://spring.io/blog/2021/09/09/spring-data-jdbc-how-to-use-custom-id-generation
  - It provides a sequence of articles
- https://docs.spring.io/spring-data/jdbc/docs/current/reference/html/#jdbc.repositories