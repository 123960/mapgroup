package actors

import akka.actor._

object WebSocketActor {
  def props(out: ActorRef, sourceActor: ActorSelection) = Props(new WebSocketActor(out, sourceActor))
}

class WebSocketActor(out: ActorRef, sourceActor: ActorSelection) extends Actor {

  def receive = {
    case "CharacGroup" => sourceActor ! "CharacGroup"
    case x             => out ! x
  }

}
