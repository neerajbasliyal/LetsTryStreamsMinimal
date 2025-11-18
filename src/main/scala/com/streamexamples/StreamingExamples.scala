package com.streamexamples

import org.apache.pekko.Done
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.IOResult
import org.apache.pekko.stream.connectors.csv.scaladsl.{CsvParsing, CsvToMap}
import org.apache.pekko.stream.scaladsl.{FileIO, Flow, Keep, Sink, Source}
import org.apache.pekko.util.ByteString

import java.nio.file.Paths
import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

object StreamingExamples extends App {

  println("Hello World!")

  // file in resources
  // name,age,location
  // fabio,43,amsterdam
  // neeraj,39,pune
  // denys,40,haarlem
  val fileContent = scala.io.Source.fromResource("members.csv")
    .getLines()
    .mkString("\n")
  val result: Future[Done] =
    Source
      .single(fileContent)
      .map(str => ByteString(str))
      .via(CsvParsing.lineScanner())
      .via(CsvToMap.toMapAsStrings())
      .runForeach(println)
  val result2: Future[Int] = Source(1 to 10)
    .runWith(Sink.fold(1)(
      // for each item that comes in, we sum it to the accumulator
      // 0 + 1
      // 1 + 2
      // 3 + 3
      // 6 + 4
      // 10 + 5
      // 15 + 6
      // 21 + 7
      // 28 + 8
      // 36 + 9
      // 45 + 10
      (accumulator, currentItem) => accumulator + currentItem
    ))

  // for csv stuff, include in build.sbt
  // "org.apache.pekko" %% "pekko-connectors-csv" % "1.0.0",

  import scala.util.{Failure, Success}
  val factorials = Source(1 to 10).scan(BigInt(1))(
    (acc, next) => acc * next
  )


  result.onComplete {
    case Success(done) => println("stream completed successfully")
    case Failure(exception) => println(s"stream completed with exception: $exception")

  }
  //
  //  val resultTick: Future[Done] =
  //    Source.tick(0.seconds, 5.seconds, "tick")
  //      .runForeach(println)
  //  resultTick.onComplete {
  //    case Success(done) => println("tick stream completed successfully")
  //    case Failure(exception) => println(s"tick stream completed with exception: $exception")
  //  }
  val resultFac: Future[IOResult] =
    factorials.map(num => ByteString(s"$num\n")).runWith(FileIO.toPath(Paths.get("factorials.txt")))

    def lineSink(filename: String): Sink[String, Future[IOResult]] =
      Flow[String].map(s => ByteString(s + "\n")).toMat(FileIO.toPath(Paths.get(filename)))(Keep.right)

    factorials.map(_.toString).runWith(lineSink("factorial2.txt"))

  result2.onComplete {
    case Success(acc) => println(s"sum of 1 to 10 is $acc")
    case Failure(exception) => println(s"sum of 1 to 10 failed with exception: $exception")
  }
  val resultPrint: Future[Done] = factorials
    .zipWith(Source(0 to 100))((num, idx) => s"$idx! = $num")
    .throttle(1, 1.second)
    .runForeach(println)

  given ExecutionContext = system.dispatcher

  // just plumbing stuff
  given system: ActorSystem = ActorSystem("example-stream-app")
}
