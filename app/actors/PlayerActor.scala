package com.amazzeo.elmtactoe.actors

import akka.actor._
import com.amazzeo.elmtactoe.models._

object PlayerActor {

  def props(outSocket: ActorRef) = {
    println("helloooo")
    Props(new PlayerActor(outSocket))
  }

}

class PlayerActor(out: ActorRef) extends Actor {

  def receive = {
    case msg: String =>
      out ! s"got it ${msg}"

    case JoinGame(gameName) =>
      out ! s"JoinGame : ${gameName}"

  }

}

