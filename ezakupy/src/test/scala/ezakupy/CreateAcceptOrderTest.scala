package ezakupy

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class CreateAcceptOrderTest extends Simulation with OrderRequests {
  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptLanguageHeader("en-US,en;q=0.5")

  val scn = scenario(
    s"""
       |Fetch products ids as an employee and select randomly two products
       |Sign in as a client and create an order containing the selected products
       |Sign in as an employee and accept the client's order
      """.stripMargin)
    .exec(signInEmployee)
    .pause(1)
    .exec(selectTwoProducts)
    .pause(1)
    .exec(signInClient)
    .pause(1)
    .exec(createOrder)
    .pause(1)
    .exec(signInEmployee)
    .pause(1)
    .exec(acceptOrder)

  setUp(scn.inject(rampUsers(10) during (5 seconds))
    .protocols(httpProtocol))
    .assertions(
      global.responseTime.max.lt(5000),
      global.successfulRequests.percent.gt(95)
    )
}