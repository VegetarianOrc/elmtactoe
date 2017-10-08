package com.amazzeo.elmtactoe.actors

import akka.actor._
import com.amazzeo.elmtactoe.models._
import com.amazzeo.elmtactoe.gameutils._

sealed trait GameProgress
case object GameNotStarted extends GameProgress
case object GameInProgress extends GameProgress
case object GameFinished extends GameProgress

object GameActor {
  case object Subscribe
  case object Unsubscribe

  def props(name: String) = Props(new GameActor(name: String))
}

case object GameFullException extends Exception("This game is full.")

class GameActor(name: String) extends Actor with GameUtils {
  import GameActor._

  private var crossPlayer: Option[ActorRef] = None
  private var circlePlayer: Option[ActorRef] = None
  private var board: Array[Array[BoardSpace]] = newBoard()
  private var nextMove: PlayerSpace = Cross
  private var gameProgress: GameProgress = GameNotStarted

  def receive = {
    case Subscribe =>
      subcribePlayer(sender())
    case p: PlayerMove =>
      handlePlayerMove(p)
  }

  def subcribePlayer(player: ActorRef) = {
    if (crossPlayer.isEmpty) {
      crossPlayer = Some(player)
      player ! SubscibeResult(name, true, Some(Cross))
      broadcast(currentState)
    } else if (circlePlayer.isEmpty) {
      circlePlayer = Some(player)
      player ! SubscibeResult(name, true, Some(Circle))
      broadcast(currentState)
      gameProgress = GameInProgress
    } else {
      player ! SubscibeResult(name, false, None)
    }
  }

  def broadcast[T](msg: T) = {
    crossPlayer.foreach(_ ! msg)
    circlePlayer.foreach(_ ! msg)
  }

  def currentState = GameState(
    board = board,
    nextMove = nextMove)

  def updateNextMove(): Unit = {
    nextMove match {
      case Cross =>
        nextMove = Circle
      case Circle =>
        nextMove = Cross
    }
  }

  def handlePlayerMove(playerMove: PlayerMove) = {
    if (gameProgress == GameInProgress && playerMove.move == nextMove) {
      board = updateBoard(board, playerMove.x, playerMove.y, playerMove.move)
      updateNextMove()
      broadcast(currentState)
      detectEndOfGame()
    }
  }

  def detectEndOfGame(): Unit = {
    detectWinner(board) match {
      case Some(winner) =>
        //someone has won
        winner match {
          case Circle =>
            broadcast(GameComplete(false, Some(Circle)))
          case Cross =>
            broadcast(GameComplete(false, Some(Cross)))
          case _ =>
          //something has gone wrong here, handle error better or make this impossible.
        }
        gameProgress = GameFinished
      case None =>
        isBoardFull(board) match {
          case true =>
            //send cats game
            broadcast(GameComplete(true, None))
            gameProgress = GameFinished
          case false =>
          //do nothing, game still in progress
        }
    }
  }

}
