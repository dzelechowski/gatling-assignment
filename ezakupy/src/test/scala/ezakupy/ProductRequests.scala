package ezakupy

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import util.Random

trait ProductRequests extends LoginRequests{
 val productsIndexNumbers = List.range(0,5)

 def selectFirstRandomNumberFromList : Iterator[Map[String, Int]] =
  Iterator.continually(Map("randomIndexNumber" ->
    productsIndexNumbers(Random.nextInt(productsIndexNumbers.size))))

 def randomNumber : Int = selectFirstRandomNumberFromList.next()("randomIndexNumber")
 val FirstRandomNumberFromList = randomNumber
 val SecondRandomNumberFromList = randomNumber

 val selectTwoProducts = http("Select Products")
   .get("/api/products/?page=0&size=10")
   .check(jsonPath(s"""$$.content[${FirstRandomNumberFromList}].id""").find.saveAs("firstProductId"))
   .check(jsonPath(s"""$$.content[${FirstRandomNumberFromList}].name""").find.saveAs("firstProductName"))
   .check(jsonPath(s"""$$.content[${SecondRandomNumberFromList}].id""").find.saveAs("secondProductId"))
   .check(jsonPath(s"""$$.content[${SecondRandomNumberFromList}].name""").find.saveAs("secondProductName"))
   .check(status.is(200))
}