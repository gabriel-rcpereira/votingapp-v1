/*import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.collection.mutable

class BasicSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  val requestBody = """{
                      | "description": "Sample Pool 2",
                      | "expiredAt": "2024-12-31T23:59:59",
                      | "options": [
                      |   {
                      |     "description": "Option 1"
                      |   },
                      |   {
                      |     "description": "Option 2"
                      |   }
                      | ]
                      |}""".stripMargin

  // Mutable list to store the session data
  val sharedSessionData = mutable.Queue[Map[String, Any]]()

  // Scenario to create the pool and extract poolId and optionIds
  val createPoolScenario = scenario("Create pool")
    .exec(http("Create Pool")
      .post("/api/v1/pools")
      .body(StringBody(requestBody)).asJson
      .check(status.is(201))
      .check(header("Location").saveAs("locationHeader")))
    .pause(7)
    .exec(session => {
      val location = session("locationHeader").as[String]
      val idPattern = """.*/(\d+)$""".r
      val id = idPattern.findFirstMatchIn(location).map(_.group(1)).getOrElse("")
      session.set("poolId", id)
    })
    .exec(http("Get Pool by ID")
      .get(session => s"/api/v1/pools/${session("poolId").as[String]}")
      .check(status.is(200))
      .check(jsonPath("$.options[*].id").findAll.saveAs("optionIds")))
    .exec(session => {
      val poolId = session("poolId").as[String]
      val optionIds = session("optionIds").as[Seq[String]]
      sharedSessionData.enqueue(Map("poolId" -> poolId, "optionIds" -> optionIds))
      session
    })

  // Feeder to provide the shared session data
  val sessionFeeder = Iterator.continually(sharedSessionData.dequeue())

  // Scenario to vote for pool options
  val voteScenario = scenario("Vote for Pool Options")
    .feed(sessionFeeder)
    .foreach("${optionIds}", "poolOptionId") {
      repeat(2) {
        exec(http("Vote for Pool Option")
          .post(session => s"/api/v1/pools/${session("poolId").as[String]}/votes")
          .body(StringBody(session => s"""{"poolOptionId": "${session("poolOptionId").as[String]}"}""")).asJson
          .check(status.is(204)))
      }
    }

  setUp(
    createPoolScenario.inject(atOnceUsers(1)), // Inject a single user to create the pool and extract options
    voteScenario.inject(rampUsers(1000).during(10.seconds)) // Inject 1000 users over 10 seconds to achieve concurrency
  ).protocols(httpProtocol)
}
*/