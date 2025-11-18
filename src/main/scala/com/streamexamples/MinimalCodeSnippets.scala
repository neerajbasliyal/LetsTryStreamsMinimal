package com.streamexamples

object MinimalCodeSnippets extends App {
  val numbers = List(1, 2, 3, 4)
  val doubled = numbers.map(_ * 2)
  println(doubled) // Output: List(2, 4, 6, 8)

  val pairs = List((2, 5), (3, -7), (20, 56))
  val products = pairs.map { case (a, b) => a * b }
  println(products)
  // Output: List(10, -21, 1120)
}
