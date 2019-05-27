package com.inneractive.networks

import java.util.Random

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.inneractive.networks.AdNetwork._
import mappers.Models.{AdNetworkResponse, AdNetworkRequest}
import org.scalacheck.Gen

/**
  * Created by dani on 08/03/16.
  */
trait AdNetwork {
  val impressionsUrls:Seq[String]

  /* import mappers.Models.ServiceJsonProtoocol._
   val route =
     path("ad") {
       post {
         parameterMap { requestParams =>
           val age: String = requestParams("age")
           val params: String = requestParams("siteId")
           entity(as[AdRequest]) {
             adRequest => complete {
               AdResponse(Gen.oneOf(impressionsUrls).sample.get, random.nextDouble(),random.nextDouble())
             }
           }
         }
       }
     }*/
}
object AdNetwork{


  implicit val actorSystem = ActorSystem("rest-api")
  val random: Random = new Random()
  implicit val actorMaterializer = ActorMaterializer()

}