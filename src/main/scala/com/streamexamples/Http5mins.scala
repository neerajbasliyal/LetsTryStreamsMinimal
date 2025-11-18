package com.streamexamples

import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.http.scaladsl.model.*
import org.apache.pekko.stream.Materializer

import java.net.URLEncoder
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

object Http5mins {
  implicit val system: ActorSystem = ActorSystem() //actors
  implicit val materializer: Materializer = Materializer(system) //streams

  import system.dispatcher // "thread pool"

  val source =
    """
      | object simpleApp {
      | val aField = 2
      | def aMethod(x: Int) =` x + 1
      | def main(args: Array[String]): Unit = println(aField)
      | }
    """.stripMargin

  val request = HttpRequest(
    method = HttpMethods.POST,
    uri = "http://markup.su/highlighter/api",
    entity = HttpEntity(
      ContentTypes.`application/x-www-form-urlencoded`,
      s"source=${URLEncoder.encode(source, "UTF-8")}&language=Scala&theme=Sunburst"
    )
  )

  def sendRequest(): Future[String] = {
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)
    val entityFuture: Future[HttpEntity.Strict] = responseFuture.flatMap(response => response.entity.toStrict(2.seconds))
    entityFuture.map(entity => entity.data.utf8String)
  }

  def main(arg: Array[String]): Unit = {
    sendRequest().foreach(println)
  }
}
