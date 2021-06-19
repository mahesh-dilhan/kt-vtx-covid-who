package io.mahesh.kotlin_vertx

import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import org.slf4j.LoggerFactory

class MainVerticle {
  private val logger = LoggerFactory.getLogger("io.mahesh.kotlin_vertx.MainVerticle")

  fun main(){

    // nonclusterdeploy();
    // Vertx.clusteredVertx();
    //Vertx vertx = nonclusterdeployment();
    Vertx.clusteredVertx(VertxOptions())
      .onSuccess { vertx: Vertx ->
        vertx.deployVerticle(CovidcasesCollector())
        vertx.eventBus()
          .consumer("who.portal",
            Handler { message: Message<JsonObject> ->
              logger.info(
                "New Covid update....{}",
                message.body().encodePrettily()
              )
            })
      }.onFailure { fail: Throwable? -> logger.error("shit..crash") }
  }

}
