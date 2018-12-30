package ezakupy

import io.gatling.core.Predef._
import io.gatling.http.Predef._

trait LoginRequests {
  val signInEmployee = http("Sign in as an employee")
    .post("/login")
    .formParam("username", "employee")
    .formParam("password", "test")
    .check(status.is(200))
    .check(jsonPath("$.role").find.is("EMPLOYEE"))

  val signInClient = http("Sign in as a client")
    .post("/login")
    .formParam("username", "client")
    .formParam("password", "test")
    .check(status.is(200))
    .check(jsonPath("$.role").find.is("CLIENT"))
    .check(jsonPath("$.id").find.saveAs("clientId"))

}


