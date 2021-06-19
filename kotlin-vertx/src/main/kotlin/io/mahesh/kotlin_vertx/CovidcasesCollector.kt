package io.mahesh.kotlin_vertx

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.json.JsonObject
import org.slf4j.LoggerFactory
import java.util.*


class CovidcasesCollector : AbstractVerticle() {
  private val logger = LoggerFactory.getLogger("io.mahesh.kotlin_vertx.CovidcasesCollector")
  private val PORT = System.getProperty("HTTP_PORT", "8888").toIntOrNull() ?: 8888
  private val countries = mutableListOf("USA", "SL", "IND", "PK", "AUS")
  private val randomcountry = Random()



  override fun start(startPromise: Promise<Void>) {
    vertx
      .createHttpServer()
      .requestHandler { req ->
        req.response()
          .putHeader("content-type", "text/plain")
          .end("Hello from Vert.x!")
      }
      .listen(8888) { http ->
        if (http.succeeded()) {
          startPromise.complete()
          println("HTTP server started on port 8888")
        } else {
          startPromise.fail(http.cause());
        }
      }
  }

  fun updateCountryStats(aLong : Long){
    
  }

   fun payload(cntry: Country): JsonObject? {
    return JsonObject()
      .put("name", cntry.name)
      .put("cases", cntry.cases)
  }

}
data class Country(var name: String, var cases: Int)
