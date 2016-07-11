package actors

import akka.actor._
import model._
import play.api.libs.json._

object WebSocketActor {
  def props(out: ActorRef, sourceActor: ActorRef) = Props(new WebSocketActor(out, sourceActor))
}

class WebSocketActor(out: ActorRef, sourceActor: ActorRef) extends Actor {

  implicit val characGroupWrites = new Writes[CharacGroup] {
    def writes(characGroup: CharacGroup) = Json.obj(
      "id"         -> characGroup.id,
      "groupDate"  -> characGroup.groupDate,
      "elements"   -> characGroup.elements,
      "affinities" -> characGroup.affinities
    )
  }

  def receive = {
    case "CharacGroup"      => sourceActor ! "CharacGroup"
    case group: CharacGroup => out ! (Json.toJson(group).toString)
  }
}
