package model;

import java.awt.Point;
import java.util.Random;

/**
 * This TTT strategy tries to prevent the opponent from winning by checking
 * for a space where the opponent is about to win. If none found, it randomly
 * picks a place to win, which an sometimes be a win even if not really trying.
 * 
 * @author mercer
 */
public class StopperAI implements TicTacToeStrategy {
  
  @Override
  public Point desiredMove(TicTacToeGame theGame) {

    // First look to block an opponent win

    // If the AI can not block, look for a win

    // If no block or win is possible, pick a move from those still available

    return null;
  }

}