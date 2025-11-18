package com.streamexamples

import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.scaladsl.{Sink, Source}
import org.apache.pekko.{Done, NotUsed}

object ReactiveTweetsExample extends App {
  val pekkoTag = Hashtag("#pekko")
  val author = Author("Tweeter1")
  //val tweets: Source[Tweet, NotUsed] = Tweet(author, 345, "lets say this is the tweet")
  //  val authors: Source[Author, NotUsed] =
  //    tweets.filter(_.hashtags.contains(pekkoTag)).map(_.author)
  //  val hashtags: Source[Hashtag, NotUsed] = tweets.mapConcat(_.hashtags.toList)

  final case class Author(handle: String)

  implicit val system: ActorSystem = ActorSystem("reactive-tweets")

  final case class Hashtag(name: String)

  //  authors.runWith(Sink.foreach(println))

  final case class Tweet(author: Author, timestamp: Long, body: String) {
    def hashtags: Set[Hashtag] =
      body
        .split(" ")
        .collect {
          case t if t.startsWith("#") => Hashtag(t.replaceAll("[^#\\w]", ""))
        }
        .toSet
  }
}
