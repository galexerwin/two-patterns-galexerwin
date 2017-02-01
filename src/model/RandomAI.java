package model;

import java.awt.Point;

/**
 * This strategy selects the first available move at random.  It is easy to beat
 * 
 * @throws IGotNowhereToGoException whenever asked for a move that is impossible to deliver
 * 
 * @author mercer
 */

// There is an intentional compile time error.  Implement this interface
public class RandomAI implements TicTacToeStrategy {

  // Randomly find an open spot while ignoring possible wins and stops.
  // This should be easy to beat as a human. 

  @Override
  public Point desiredMove(TicTacToeGame theGame) {
 
 
    return new Point(2,2);
  }
}