package mappers

import spray.json.{JsString, JsArray, DefaultJsonProtocol}

/**
  * Created by dani on 08/03/16.
  */
object Models {
  case class AdNetworkRequest(secretKey: String)
  case class AdNetworkResponse(adUrl: String, matching: Double, price: Double)
  object NetworksJsonProtocol extends DefaultJsonProtocol {
    implicit val adRequestProtocol = jsonFormat1(AdNetworkRequest)
    implicit val adResponseProtocol = jsonFormat3(AdNetworkResponse)

  }

  case class AdServerRequest(publisherKey: String)
  case class AdServerResponse(adUrl: String)
  object ServerJsonProtocol extends DefaultJsonProtocol {
    implicit val adServerRequestProtocol = jsonFormat1(AdServerRequest)
    implicit val adServerResponseProtocol = jsonFormat1(AdServerResponse)
  }
}
