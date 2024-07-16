//import io.gatling.core.Predef._
//import io.gatling.http.Predef._
//import scala.concurrent.duration._
//
//
//class VoteForPoolsSimulation extends Simulation {
//
//  val httpProtocol = http
//    .baseUrl("http://localhost:8080")
//    .acceptHeader("application/json")
//    .contentTypeHeader("application/json")
//
//  val poolFeeder = csv("poolData.csv").circular()
//
//  val voteScenario = scenario("Vote for Pool Options")
//    .feed(poolFeeder)
//    .exec(http("Vote for Pool Option")
//      .post(session => s"/api/v1/pools/${session("poolId").as[String]}/votes")
//      .body(StringBody(session => s"""{"poolOptionId": "${session("poolOptionId").as[String]}"}""")).asJson
//      .check(status.is(204)))
//
//  setUp(
//    voteScenario.inject(rampUsers(1000).during(10.seconds)) // Adjust as needed for your load test
//  ).protocols(httpProtocol)
//}
