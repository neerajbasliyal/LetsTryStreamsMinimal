package com.streamexamples

object AsyncNonBlockingExplain {

  def blockingFunction(x: Int) : Int = {
    Thread.sleep(10000)
    x + 42
  }
  blockingFunction(5)
  val meaningOfLife = 42

  def main(args: Array[String]) : Unit = {}

}
