package ezakupy

import java.text.SimpleDateFormat
import java.util.Calendar

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class CreateAcceptOrder extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptLanguageHeader("en-US,en;q=0.5")
  val format = new SimpleDateFormat("d-M-y h:m:s")

  val scn = scenario("A client creates an order that contains 2 items and" +
    " an employee accepts the order")
    .exec(http("Sign in as a client")
      .post("/login")
      .formParam("""username""", """client""")
      .formParam("""password""", """test""")
      .check(status.is(200))
    )
    .pause(3)
    .exec(http("Create an order that contains 2 items")
      .post("/api/orders/")
      .body(StringBody(
        s"""{
           | "status":"DRAFT",
           | "items":[
           |   {
           |     "id":0,
           |     "isEditing":false,
           |     "product": {
           |       "id":"5c1a9e73dac7031a801806fc",
           |       "name":"Testowy produkt 1"
           |     },
           |   "quantity":1
           |   },
           |   {
           |     "id":1,
           |     "isEditing":false,
           |     "product": {
           |       "id":"5c1a9e73dac7031a80180700",
           |       "name":"Testowy produkt 5"
           |     },
           |   "quantity":1
           |   }
           |  ]
           | }""".stripMargin)).asJson
      .check(status.is(200))
      .check(regex( """"id":"([0-9a-zA-Z]*)"""").saveAs("orderId")))
    .pause(3)
    .exec(http("Sign in as an enployee")
      .post("/login")
      .formParam("""username""", """employee""")
      .formParam("""password""", """test""")
      .check(status.is(200))
    )
  /* Zmienna ${orderId} nie kompiluje się w body.
     Zmienną należy zapisać przy pomocy innej metody niż saveAs.
      .pause(3)
      .exec(http("an employee accepts the order")
        .put("/api/orders/${orderId}")
        .body(StringBody(
          s"""{
             | "id":"${orderId}",
             | "confirmationDate": "${format.format(Calendar.getInstance().getTime())}",
             | "status":"ACCEPTED"
             | }""".stripMargin)).asJson)
  */
  setUp(scn.inject(atOnceUsers(1)).protocols(httpProtocol))
}

