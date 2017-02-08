package model;
//import classes
import java.awt.Point;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/*
 * Author: Alex Erwin
 * Purpose: Strategy to prevent the other player from winning by blocking their moves or randomly selecting a space to move to. Includes
 * an odds of winning class and comparable list of odds to determine the best odds of blocking the next move. This class isn't concerned
 * with winning, only ties.
 * Throws: IGotNowhereToGoException if there are no more moves left
 */
public class StopperAI implements TicTacToeStrategy {
	  // instance vars
	  private int middleSquare;
	  @Override
	  public Point desiredMove(TicTacToeGame theGame) {
		  // variables
		  char player	 = theGame.getCurrentPlayerChar();
		  char[][] board = theGame.getTicTacToeBoard();
		  int gameSize	 = theGame.size();
		  Point suggestedMove;
		  double bestWinOdds;
		  int x = 0, y = 0, max = 0;
		  // odds of winning objects for the available win types
		  oddsOfWinning column, row, ulDiagonal, llDiagonal;
		  // contains our odds which will be sorted
		  List<oddsOfWinning> odds = new ArrayList<>();
		  // set the middle
		  middleSquare = gameSize / 2;
		  // always go after the center if the board is an odd numbered size and the space isn't taken
		  if (gameHasCenter(gameSize) && theGame.available((middleSquare), (middleSquare)))
			  return new Point((middleSquare), (middleSquare));
		  // iterate over the entire game board
		  for (int i = 0; i < theGame.size(); i++) {
			  for (int j = 0; j < theGame.size(); j++) {
				  // if this token belongs to the other player
				  if (board[i][j] == player) {
					  // new odds of winning objects
					  column     = new oddsOfWinning();
					  row	     = new oddsOfWinning();
					  ulDiagonal = new oddsOfWinning();
					  llDiagonal = new oddsOfWinning();
					  // check the column
					  if (checkCol(board, player, gameSize, j, column))
						  odds.add(column);
					  // check the row
					  if (checkRow(board, player, gameSize, i, row))
						  odds.add(row);
					  // check the upper left diagonal
					  if (checkULDiagonal(board, player, gameSize, i, j, ulDiagonal))
						  odds.add(ulDiagonal);
					  // check the lower left diagonal
					  if (checkLLDiagonal(board, player, gameSize, i, j, llDiagonal))
						  odds.add(llDiagonal);					  
				  }
			  }
		  }
		  // sort the list of odds
		  Collections.sort(odds, new sortByOdds());
		  // get the best chance to block and the next move
		  suggestedMove = odds.get(odds.size() - 1).getNextMove();
		  bestWinOdds   = odds.get(odds.size() - 1).getChances();
		  // check if we want to use the suggested move or a random point
		  if (bestWinOdds > 50) {
			  return suggestedMove;
		  } else {
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
		  }
		  // if there are no more moves
		  throw new IGotNowhereToGoException("No more moves left!");		  
	  }
	  // determine if the game has a center space
	  private boolean gameHasCenter(int size) {
		  return ((size % 2) == 1);
	  }
	  // check if the player can win the upper left to lower right diagonal. false means zero chance because they are blocked
	  private boolean checkULDiagonal(char[][] board, char player, int size, int px, int py, oddsOfWinning odds) {
		  // variables
		  int  sum = 0, x = -1, y = -1;
		  boolean inRange = false;
		  // sanity check if this is in the range
		  for (int i = 0; i < size; i++) {
			  if (i == px && i == py) {
				  // item is in range
				  inRange = true;
				  // break
				  break;
			  }		  
		  }
		  // check if in the range
		  if (!inRange) return false;
		  // upper left to lower right diagonal
		  for (int i = 0; i < size; i++) {
			  if (board[i][i] == player) {
				  // increment the number of moves the player has on this diagonal
				  sum++;
			  } else if (board[i][i] == '_') {
				  // a space was encountered, save it
				  if (x == -1 && y == -1) { // find the closest space to the last move
					  x = i;
					  y = i;				  
				  }
			  } else {
				  // our token was located in the path. player can't win
				  return false;
			  } 
		  } 
		  // set the odds of winning this round from this diagonal
		  odds.setChances((double)sum/(double)size);
		  // set the recommended move
		  odds.setNextMove(x, y);
		  // return default
		  return true;	  
	  }
	  // check if the player can win the lower left to upper right diagonal. false means zero chance because they are blocked
	  private boolean checkLLDiagonal(char[][] board, char player, int size, int px, int py, oddsOfWinning odds) {
		  // variables
		  int  sum = 0, x = -1, y = -1;
		  boolean inRange = false;
		  // sanity check if this is in the range
		  for (int i = (size - 1), j = 0; i >= 0 && j < size; i--, j++) {
			  if (i == px && j == py) {	
				  // item is in range
				  inRange = true;
				  // break
				  break;
			  }		  
		  }
		  // check if in the range
		  if (!inRange) return false;
		  // upper right to lower left diagonal
		  for (int i = (size - 1), j = 0; i >= 0 && j < size; i--, j++) {
			  if (board[i][j] == player) {
				  // increment the number of moves the player has on this diagonal
				  sum++;
			  } else if (board[i][j] == '_') {
				  // a space was encountered, save it
				  if (x == -1 && y == -1) { // find the closest space to the last move
					  x = i;
					  y = j;				  
				  }
			  } else {
				  // our token was located in the path. player can't win
				  return false;
			  } 
		  }
		  // set the odds of winning this round from this diagonal
		  odds.setChances((double)sum/(double)size);
		  // set the recommended move
		  odds.setNextMove(x, y);
		  // return default
		  return true;	
	  }
	  // check if the player can win the row. false means zero chance because they are blocked
	  private boolean checkRow(char[][] board, char player, int size, int row, oddsOfWinning odds) {
		  // variables
		  int  sum = 0, x = -1, y = -1;
		  // iterate over the column
		  for (int i = 0; i < size; i++) {
			  if (board[row][i] == player) {
				  // increment the number of moves the player has on this column
			  sum++;			  
		  } else if (board[row][i] == '_') {
			  // a space was encountered, save it
			  if (x == -1 && y == -1) { // find the closest space to the last move
				  x = row;
				  y = i;				  
			  }
		  } else {
			  // our token was located in the path. player can't win this column
				  return false;
			  } 
		  }
		  // set the odds of winning this round from this column
		  odds.setChances((double)sum/(double)size);
		  // set the recommended move
		  odds.setNextMove(x, y);
		  // return default
		  return true;
	  }
	  // check if the player can win the column. false means zero chance because they are blocked
	  private boolean checkCol(char[][] board, char player, int size, int col, oddsOfWinning odds) {
		  // variables
		  int  sum = 0, x = -1, y = -1;
		  // iterate over the column
		  for (int i = 0; i < size; i++) {
			  if (board[i][col] == player) {
				  // increment the number of moves the player has on this column
			  sum++;			  
		  } else if (board[i][col] == '_') {
			  // a space was encountered, save it
			  if (x == -1 && y == -1) { // find the closest space to the last move
				  x = i;
				  y = col;				  
			  }
		  } else {
			  // our token was located in the path. player can't win this column
				  return false;
			  } 
		  }
		  // set the odds of winning this round from this column
		  odds.setChances((double)sum/(double)size);
		  // set the recommended move
		  odds.setNextMove(x, y);
		  // return default
		  return true;
	  }
	  // randomizer credit to Rick Mercer
	  private int randomInt(int low, int high) {
		  return low + (int) (Math.random() * (high - low + 1));
	  }	  
	  // odds of winning class contains the recommended move and the chances of the other player winning based on this move
	  private class oddsOfWinning  {
		  // class vars
		  private double chances;
		  private Point next;
		  // constructor sets chances at zero and assigns a dummy point
		  public oddsOfWinning() {
			  // set odds at zero
			  this.chances = 0;
			  // set a default point
			  next = new Point(-1,-1);
		  }
		  // set the odds of winning
		  public void setChances(double chance) {
			  this.chances = chance * 100;
		  }
		  // set the recommended point
		  public void setNextMove(int x, int y) {
			  this.next.setLocation(x, y);
		  }
		  // get the chances of winning
		  public double getChances() {
			  return chances;
		  }
		  // get the next move
		  public Point getNextMove() {
			  return next;
		  }
	  }
	  // private class to sort the odds
	  private class sortByOdds implements Comparator<oddsOfWinning> {
		  @Override
		  public int compare(oddsOfWinning a, oddsOfWinning b) {
			  // return the comparison to the sorting routine
			  return (int)a.getChances() - (int)b.getChances();
		  }		  
	  }
}