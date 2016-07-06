package source

import akka.actor._
import com.github.nscala_time.time.Imports._

import model._

object SourceActor {

  def props = Props[SourceActor]

}

class SourceActor extends Actor {
  def receive = {
    case "CharacGroup" =>
      sender() ! new CharacGroup("GR00X", new DateTime(), List("1"), List("sexo:M|UF:MG"))
  }
}
