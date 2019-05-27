package com.inneractive.networks

import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import com.inneractive.networks.AdNetwork._
import mappers.Models.{AdNetworkRequest, AdNetworkResponse, NetworksJsonProtocol}
import org.scalacheck.Gen
/**
  * Created by dani on 08/03/16.
  */
object SababaAdNetwork extends App with AdNetwork{


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
              Thread.sleep(1000)
              if(siteId.equals("sheker")) AdNetworkResponse("", 0, 0) else  Gen.frequency((1, AdNetworkResponse("", 0, 0)), (3, AdNetworkResponse(Gen.oneOf(impressionsUrls).sample.get, random.nextDouble(),random.nextDouble()))).sample.get

            }
          }
        }
      }
    }

  Http().bindAndHandle(route, "0.0.0.0", 8083)
  override val impressionsUrls: Seq[String] = Seq("http://www.naturephoto.co.il/Articles_pics/Presentation01.jpg",
    "http://www.naturephoto.co.il/Articles_pics/Presentation05.jpg",
    "http://friends.kz/uploads/posts/2008-03/thumbs/1206250393_tatulinski_002.jpg")
}
