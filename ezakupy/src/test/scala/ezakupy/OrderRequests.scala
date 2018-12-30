package ezakupy

import io.gatling.core.Predef._
import io.gatling.http.Predef._

trait OrderRequests extends ProductRequests{
  val createOrder = http("Create an order containing two items")
    .post("/api/orders/")
    .body(StringBody(
      """{
         | "status":"DRAFT",
         | "items":[
         |   {
         |     "id":0,
         |     "isEditing":false,
         |     "product":
         |      {
         |        "id":"${firstProductId}",
         |        "name":"${firstProductName}"
         |      },
         |     "quantity":1
         |   },
         |   {
         |     "id":1,
         |     "isEditing":false,
         |     "product":
         |      {
         |        "id":"${secondProductId}",
         |        "name":"${secondProductName}"
         |      },
         |     "quantity":1
         |   }
         |  ]
         | }""".stripMargin)).asJson
    .check(status.is(200))
    .check(jsonPath("$.id").saveAs("orderId"))
    .check(jsonPath("$.items[0].product.id").find.is("${firstProductId}"))
    .check(jsonPath("$.items[0].product.name").find.is("${firstProductName}"))
    .check(jsonPath("$.items[1].product.id").find.is("${secondProductId}"))
    .check(jsonPath("$.items[1].product.name").find.is("${secondProductName}"))
    .check(jsonPath("$.status").find.is("DRAFT"))

  val acceptOrder = http("Accept the order")
    .put("/api/orders/${orderId}")
    .body(StringBody(
      """{
         |  "id":"${orderId}",
         |  "items":[
         |    {
         |      "product":
         |        {
         |          "id":"${firstProductId}",
         |          "name":"${firstProductName}"
         |        },
         |      "quantity":1
         |    },
         |    {
         |    "product":
         |      {
         |        "id":"${secondProductId}",
         |        "name":"${secondProductName}"
         |      },
         |      "quantity":1
         |    }
         |  ],
         |  "client":
         |    {
         |      "id":"${clientId}",
         |      "name":"Client Test 1"
         |    },
         |  "status":"ACCEPTED"}
        """.stripMargin)).asJson
    .check(status.is(200))
    .check(jsonPath("$.items[0].product.id").find.is("${firstProductId}"))
    .check(jsonPath("$.items[0].product.name").find.is("${firstProductName}"))
    .check(jsonPath("$.items[1].product.id").find.is("${secondProductId}"))
    .check(jsonPath("$.items[1].product.name").find.is("${secondProductName}"))
    .check(jsonPath("$.status").find.is("ACCEPTED"))
}