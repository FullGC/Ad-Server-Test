package com.inneractive.networks

import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import com.inneractive.networks.AdNetwork._
import org.scalacheck.Gen
import mappers.Models._
/**
  * Created by dani on 08/03/16.
  */
object YourAdNetwork extends App with AdNetwork{


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

    Http().bindAndHandle(route, "0.0.0.0", 8081)
  override val impressionsUrls: Seq[String] = Seq("http://studentsforliberty.org/wp-content/uploads/2014/09/South_Park_banner.png",
    "https://fanart.tv/fanart/tv/75897/clearart/SouthPark-75897-3.png",
    "https://pbs.twimg.com/profile_images/677661321173868545/3VOoZCiJ.jpg")



}
