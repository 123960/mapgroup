package mapgroup.actors

import akka.actor._
import play.api.libs.json._
import com.github.nscala_time.time.Imports._

import mapgroup.model._
import mapgroup.local._
import scala.util.Random

class SourceActor extends Actor {

  implicit val characGroupWrites = new Writes[CharacGroup] {
    def writes(characGroup: CharacGroup) = Json.obj(
      "id"         -> characGroup.id,
      "groupDate"  -> characGroup.groupDate,
      "elements"   -> characGroup.elements,
      "percent "   -> characGroup.percent,
      "affinities" -> characGroup.affinities
    )
  }

  def receive = {
    case "CharacGroup" =>
      val group = MapGroupEngine.characGroupByCharacValue(Random.shuffle(MapGroupEngine.elements).take(10))
      if (!group.elements.isEmpty) sender() ! Json.toJson(group).toString
      else println("[SourceActor] empty group")
  }
}
