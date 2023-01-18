object Main extends App {
  val dimension = 3
  val screen =
    """|0:100,125, 56,200
       |1:255,  0,  0
       |2:  0,135,200
       |3:220, 12,  0,100
       |4: 45, 97,  0
       |5:  0,  0,  0
       |6:  0,  0,  0,0
       |7:  0,  0,  0
       |8:255,255,255
       |""".stripMargin

  case class Position(y: Int, x: Int)
  case class Properties(red: Int, green: Int, blue: Int, transparent: Option[Int])
  case class Pixel(position: Position, properties: Properties)

  val pixels = screen.split("\n")
    .map(_.replace(" ", ""))
    .map { line =>
      line.trim match {
        case s"$id:$red,$green,$blue,$transparent" => Pixel(Position(id.toInt / dimension, id.toInt % dimension), Properties(red.toInt, green.toInt, blue.toInt, transparent.toIntOption))
        case s"$id:$red,$green,$blue" => Pixel(Position(id.toInt / dimension, id.toInt % dimension), Properties(red.toInt, green.toInt, blue.toInt, None))
      }
    }
    .toList

  pixels.foreach(println)

  val maxRedByRow = pixels.groupBy(_.position.y)
    .map { case (row, pixels) => (row, pixels.maxBy(_.properties.red)) }
  println("Max red by row: " + maxRedByRow)

  val maxtransparentByRow = pixels.groupBy(_.position.y)
    .map { case (row, pixels) => (row, pixels.maxBy(_.properties.transparent).position.x) }
  println("Max transparent by row: " + maxtransparentByRow)
}