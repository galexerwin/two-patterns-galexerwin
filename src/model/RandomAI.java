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
	  // variables
	  int gameSize = theGame.size();
	  int x = 0, y = 0, max = 0;
	  // try empty spaces at random
	  do {
		  // set x and y to random integers
		  x = randomInt(0, gameSize - 1);
		  y = randomInt(0, gameSize - 1);
		  // check if an open space
		  if (theGame.available(x, y))
			  return new Point(x,y);
		  // increment max
		  max++;
	  } while (max < ((gameSize * gameSize) * 2));
	  // iterate over the rows and columns to find an empty space
	  for (int i = 0; i < gameSize; i++){		// rows
		  for (int j = 0; j < gameSize; j++) {  // columns
			  if (theGame.available(i, j))
				  return new Point(i,j);
		  }
	  }	
	  // if there are no more moves
	  throw new IGotNowhereToGoException("No more moves left!");
  }
  // randomizer credit to Rick Mercer
  private int randomInt(int low, int high) {
	  return low + (int) (Math.random() * (high - low + 1));
  }	    
}