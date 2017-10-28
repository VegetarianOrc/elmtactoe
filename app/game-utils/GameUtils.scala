package com.amazzeo.elmtactoe.gameutils

import com.amazzeo.elmtactoe.models._
import scala.util.{ Try, Success, Failure }

trait GameUtils {

  def newBoard(): Array[Array[BoardSpace]] = {
    Array(
      Array(Empty, Empty, Empty),
      Array(Empty, Empty, Empty),
      Array(Empty, Empty, Empty))
  }

  private[this] def isWinner(combination: Array[BoardSpace]): Tuple2[Boolean, BoardSpace] = {
    if (combination.forall(_ == Cross)) {
      (true, Cross)
    } else if (combination.forall(_ == Circle)) {
      (true, Circle)
    } else {
      (false, Empty)
    }
  }

  private[this] def getColumn(board: Array[Array[BoardSpace]], colNum: Int): Array[BoardSpace] = {
    board.map(_(colNum))
  }

  def detectWinner(board: Array[Array[BoardSpace]]): Option[BoardSpace] = {
    val rows: Array[Tuple2[Boolean, BoardSpace]] = board.map(isWinner _)
    val columns: Array[Tuple2[Boolean, BoardSpace]] = Array(getColumn(board, 0), getColumn(board, 1), getColumn(board, 2)).map(isWinner _)

    val topBottomDiag = Array(
      board(0)(0),
      board(1)(1),
      board(2)(2))

    val bottomTopDiag = Array(
      board(0)(2),
      board(1)(1),
      board(2)(0))

    val diagResults = Array(topBottomDiag, bottomTopDiag).map(isWinner _)

    (rows ++: columns ++: diagResults).filter(_._1 == true).headOption.map(_._2)

  }

  def isBoardFull(board: Array[Array[BoardSpace]]): Boolean = {
    board.map(_.forall(_ != Empty)).reduce(_ && _)
  }

  def updateBoard(board: Array[Array[BoardSpace]], x: Int, y: Int, move: PlayerSpace): Array[Array[BoardSpace]] = {
    Try(board(y)(x)) match {
      case Success(space) if space == Empty =>
        val updatedRow = board(y).updated(x, move)
        board.updated(y, updatedRow)
      case _ =>
        //if we got an error or if the space is not empty
        board
    }
  }

}
