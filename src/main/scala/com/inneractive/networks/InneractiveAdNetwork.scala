package com.inneractive.networks

import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import com.inneractive.networks.AdNetwork._
import mappers.Models.{AdNetworkResponse, AdNetworkRequest, NetworksJsonProtocol}
import org.scalacheck.Gen
/**
  * Created by dani on 08/03/16.
  */
object InneractiveAdNetwork extends App with AdNetwork{


  import NetworksJsonProtocol._
  val route =
    path("ad") {
      post {
        parameterMap { requestParams =>
          val width: String = requestParams("width")
          val height: String = requestParams("height")
          val siteId: String = requestParams("siteId")
          require(width.toInt > 0);require(height.toInt > 0); require(siteId.length > 0)
          entity(as[AdNetworkRequest]) {
            adRequest => complete {
              if(siteId.equals("sheker")) AdNetworkResponse("", 0, 0) else  Gen.frequency((1, AdNetworkResponse("", 0, 0)), (3, AdNetworkResponse(Gen.oneOf(impressionsUrls).sample.get, random.nextDouble(),random.nextDouble()))).sample.get
            }
          }
        }
      }
    }

  Http().bindAndHandle(route, "0.0.0.0", 8082)
  override val impressionsUrls: Seq[String] = Seq("http://www.naturephoto.co.il/Articles_pics/Presentation01.jpg",
    "http://image.slidesharecdn.com/akka-scala-150402033659-conversion-gate01/95/akka-and-scala-inneractive-2-638.jpg?cb=1427945990",
    "http://cdn2.hubspot.net/hub/438089/hubfs/case-studies/Inneractive-and-Databricks-logos.png?noresize&t=1458254826875&width=200")

}
