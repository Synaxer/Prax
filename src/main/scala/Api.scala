import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{ContentTypes, FormData, HttpEntity, HttpMethods, HttpRequest}
import akka.http.scaladsl.unmarshalling.Unmarshal

import java.net.URLEncoder
import scala.io.StdIn.readLine


object Api extends App {
  implicit val as = ActorSystem()
  implicit val ec = as.dispatcher

  implicit class MapToJson[V](params: Map[String, V]) {
    def toUrlParams: String = params.map { case (k, v) => s"$k=$v" }.mkString("&")
  }

  val q = readLine("Enter text to translate: ")
  val target = readLine("Enter target language (e.g. sv): ")

  val params = Map(
    "q" -> q,
    "target" -> target
  )

  val formData = FormData(params).toEntity

  val request = HttpRequest(
    method = HttpMethods.POST,
    uri = s"https://google-translate1.p.rapidapi.com/language/translate/v2",
    headers = Seq(
      RawHeader("X-RapidAPI-Key", "f6f990a8ffmsh92f3750c4cc5759p10ce73jsn738bfe8683f4"),
      RawHeader("X-RapidAPI-Host", "google-translate1.p.rapidapi.com"),
      RawHeader("Accept-Encoding", "application/gzip"),
      RawHeader("content-type", "application/x-www-form-urlencoded'")
    ),
    entity = formData
  )
  val performRequestFut = for {
    response <- Http().singleRequest(request)
    status = response.status
    body <- Unmarshal(response.entity).to[String]
    _ = response.entity.discardBytes()
  } yield println(s"status: $status, body: $body")
  performRequestFut.andThen(_ => as.terminate()) // we do not need Await.result because of as running
}