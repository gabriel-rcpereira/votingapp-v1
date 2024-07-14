import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import java.io.PrintWriter

class CreatePoolsSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  val requestBody = """{
                      | "description": "Sample Pool 1",
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

  val setupPoolAndPoolOptions = scenario("Create pool")
    .exec(http("Create Pool")
      .post("/api/v1/pools")
      .body(StringBody(requestBody)).asJson
      .check(status.is(201))
      .check(header("Location").saveAs("locationHeader")))
    .pause(1)
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

      val pw = new PrintWriter(new java.io.File("poolData.csv"))
      pw.println("poolId,poolOptionId")

      optionIds.foreach(poolOptionId => pw.println(s"$poolId,$poolOptionId"))

      pw.close()
      session
    })

  val poolFeeder = csv("poolData.csv").circular()

  val voteScenario = scenario("Vote for Pool Options")
    .feed(poolFeeder)
    .exec(http("Vote for Pool Option")
      .post(session => s"/api/v1/pools/${session("poolId").as[String]}/votes")
      .body(StringBody(session => s"""{"poolOptionId": "${session("poolOptionId").as[String]}"}""")).asJson
      .check(status.is(204)))
//    .pause(1)
    .exec(http("Check pool result")
      .get(session => s"/api/v1/pools/${session("poolId").as[String]}/votes")
      .check(status.is(200)))

  setUp(
    setupPoolAndPoolOptions.inject(atOnceUsers(1)),
    voteScenario.inject(rampUsers(1000).during(20.seconds)) // Adjust as needed for your load test
  ).protocols(httpProtocol)
}
