package com.inneractive.solution

import akka.actor.{Actor, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import dispatch.{as => dispatchAs, url}
import mappers.Models._
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

/**
  * Created by dani on 08/03/16.
  */
object AdServer extends App{

  implicit val actorSystem = ActorSystem("rest-api")
  implicit val actorMaterializer = ActorMaterializer()
  import NetworksJsonProtocol._
  import ServerJsonProtocol._


  val config = ConfigFactory.load
  val criteria = config.getConfig("adServer").getString("criteria")

  val route =
    path("adServer" / "ad") {
      post {
        parameterMap { requestParams =>
          val width: String = requestParams("width");val height: String = requestParams("height");val siteId: String = requestParams("siteId")
          if(!(width.toInt > 0 & height.toInt > 0 & siteId.length > 0)) throw AdServerException("bad input")
          entity(as[AdServerRequest]) {
            adRequest => complete {
                val requestNetwork1 = (url("http://localhost:8081") / "ad").addQueryParameter("width", width).addQueryParameter("height", height).addQueryParameter("siteId", siteId).addHeader("content-type", "application/json")
                val requestNetwork2 = (url("http://localhost:8082") / "ad").addQueryParameter("width", width).addQueryParameter("height", height).addQueryParameter("siteId", siteId).addHeader("content-type", "application/json")
                val requestNetwork3 = (url("http://localhost:8083") / "ad").addQueryParameter("width", width).addQueryParameter("height", height).addQueryParameter("siteId", siteId).addHeader("content-type", "application/json")
                val futureResponse =  Future.sequence(Seq(requestNetwork1, requestNetwork2, requestNetwork3) map(request => dispatch.Http(request.underlying(_.setBody(AdNetworkRequest("111111").toJson.toString())).POST OK dispatchAs.Response(r => r))))
                futureResponse.map(responseSeq => {
                  val ads = responseSeq.map(response => response.getResponseBody.parseJson.convertTo[AdNetworkResponse])
                  val winner = ads.filterNot(_.adUrl.isEmpty).sortBy(ad =>  if (criteria.equals("match")) ad.matching else ad.price)
                  winner.headOption.map(ad => {adStatsCache ! Increment();  AdServerResponse(ad.adUrl)}).getOrElse(AdServerResponse("https://media.glassdoor.com/sqll/755816/inneractive-squarelogo-1436766311679.png"))
                })
            }
          }
        }
      }
    }

  Http().bindAndHandle(route, "0.0.0.0", 8084)

  val system = ActorSystem("system")
  val adStatsCache = system.actorOf(Props(classOf[AdStatsCache]))
  system.scheduler.scheduleOnce(1 minutes) {adStatsCache ! Get()}
}

case class AdServerException(message: String) extends IllegalArgumentException(message: String)

class AdStatsCache extends Actor {

  var winners: Int = 0

  override def receive: Receive = {
    case Increment() => winners = winners +1
    case Get() => println(winners)
  }
}

case class Increment()
case class Get()