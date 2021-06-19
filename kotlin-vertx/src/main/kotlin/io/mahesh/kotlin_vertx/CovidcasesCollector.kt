package io.mahesh.kotlin_vertx

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import org.slf4j.LoggerFactory

class CovidcasesCollector : AbstractVerticle() {
  private val logger = LoggerFactory.getLogger("io.mahesh.kotlin_vertx.CovidcasesCollector")
  private val PORT = System.getProperty("HTTP_PORT", "8888").toIntOrNull() ?: 8888

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
}
