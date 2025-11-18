package com.streamexamples

import org.apache.pekko.{Done, NotUsed}
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.scaladsl.{Flow, RunnableGraph, Sink, Source, Keep}

import scala.concurrent.{ExecutionContext, Future}

object JediValuesStreams extends App{
  //reactive streams
  //sources flows sinks

  implicit val system: ActorSystem = ActorSystem()
  import system.dispatcher

  val nums = List(1, 2, 3)
  val somevalue = nums.map(i => i * 2) // long form
  val someothervalue = {
    nums.map(_ * 2)
  } // short form
  println(somevalue)
  println(someothervalue)

//
//  val s = "Hello"
//  val p = Person("Al", "Pacino")
//  val sum = nums.reduceLeft(_ + _)
//  val y = for (i <- nums) yield i * 2
//  val z = nums
//    .filter(_ > 100)
//    .filter(_ < 10_000)
//    .map(_ * 2)

//  val s = "Hello"
//  val p = Person("Al", "Pacino")
//  val sum = nums.reduceLeft(_ + _)
//  val y = for i <- nums yield i * 2
//  val z = nums
//    .filter(_ > 100)
//    .filter(_ < 10_000)
//    .map(_ * 2)

  //streaming components
  val source: Source[Int, NotUsed] = Source(1 to 1000)
  val flow: Flow[Int, Int, NotUsed] = Flow[Int].map(x => x*2)
  val sink: Sink[Int, Future[Done]] = Sink.foreach[Int](println)
  val summingSink: Sink[Int, Future[Int]] = Sink.fold[Int, Int](0)((currentSum, incomingElement) => currentSum+incomingElement)

//  val graph: RunnableGraph[NotUsed] = source.via(flow).to(sink)
//  val anotherGraph: RunnableGraph[Future[Done]] = source.via(flow).toMat(sink)(Keep.right)
//  //Jedi values
  import scala.util.{Failure, Success}

  //val jediValue = graph.run() //graph alive
  //val anotherJediValue = anotherGraph.run()
  //anotherJediValue.onComplete(_ => println("stream is done"))

  val sumFuture: Future[Int] = source.via(flow).toMat(summingSink)(Keep.right).run()
  sumFuture.foreach(println)
}
