package io.mahesh.kotlin_vertx

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import org.slf4j.LoggerFactory
import java.util.*


class CovidcasesCollector : AbstractVerticle() {
  private val logger = LoggerFactory.getLogger("io.mahesh.kotlin_vertx.CovidcasesCollector")
  private val PORT = System.getProperty("HTTP_PORT", "8888").toIntOrNull() ?: 8888
  private val countries = mutableListOf("USA", "SL", "IND", "PK", "AUS")
  private val randomcountry = Random()

  private val data = mutableMapOf<String, Country>()

  override fun start(startPromise: Promise<Void>) {
    vertx.setPeriodic(1000) { aLong: Long? -> updateCountryStats(aLong!!) }

    val router = Router.router(vertx)
    router["/list"].handler { routingContext: RoutingContext? -> list(routingContext!!) }
  }

  fun list(routingContext: RoutingContext){
    val list = JsonArray()
    val jsonObject = JsonObject()
    data.forEach { (k: String?, v: Country) ->
      list.add(
        JsonObject()
          .put(k, v.cases).toString()
      )
    }
    routingContext.response()
      .putHeader("Content-Type", "application/json")
      .setStatusCode(200)
      .end(JsonObject().put("data", list).encode())
  }

  fun updateCountryStats(aLong : Long){
    val idx = randomcountry.nextInt(countries.size)
    val name = countries[idx]
    val postivecases = randomcountry.nextInt(1000)
    val cntry = Country(name, postivecases)
    data.put(name, cntry)
    logger.info("{}", cntry.cases.toString() + "-" + cntry.name)
    vertx.eventBus().publish("who.portal", payload(cntry))
  }

   fun payload(cntry: Country): JsonObject? {
    return JsonObject()
      .put("name", cntry.name)
      .put("cases", cntry.cases)
  }

}
data class Country(var name: String, var cases: Int)
