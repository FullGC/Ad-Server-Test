package com.inneractive.networks

import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import org.scalatest.{Matchers, WordSpec}


/**
  * Created by dani on 08/03/16.
  */
class RestSpec extends WordSpec with Matchers with ScalatestRouteTest{
  "Customer API" should {
    "Posting to /customer should add the customer" in {

      val jsonRequest = ByteString(
        s"""
           |{
           |    "secretKey":"sad"
           |}
        """.stripMargin)

      val postRequest = HttpRequest(
        HttpMethods.POST,
        uri = "/ad",
        entity = HttpEntity(MediaTypes.`application/json`, jsonRequest))


      postRequest ~> SababaAdNetwork.route ~> check {
         status.isSuccess() shouldEqual true
      }
    }

  }
}
