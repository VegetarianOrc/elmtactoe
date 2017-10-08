package com.amazzeo.elmtactoe.actors

import akka.actor._
import java.util.concurrent.ConcurrentHashMap

object GameCache {
  private[this] val games = new ConcurrentHashMap[String, ActorRef]()

  def get(name: String)(implicit actorRefFactory: ActorRefFactory): ActorRef = {
    Option(games.get(name)) match {
      case Some(gameActor) =>
        gameActor

      case None =>
        val newGame = actorRefFactory.actorOf(GameActor.props(name))
        games.putIfAbsent(name, newGame)
        newGame
    }
  }

}
