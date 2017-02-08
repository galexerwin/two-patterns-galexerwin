package view;
// import classes
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JPanel;
import controller.OurObserver;
import model.ComputerPlayer;
import model.TicTacToeGame;
/*
 * Author: Alex Erwin
 * Purpose: TextAreaView is a second observer and an alternative to ButtonView. TextAreaView 
 * requires the user to enter a valid integer for the row and column fields and executes that move if 
 * the integers are in range and available. Throws exceptions on input and unavailable moves.
 */
public class TextAreaView  extends JPanel implements OurObserver {
	  // instance vars
	  private TicTacToeGame theGame;
	  private JButton stateButton = new JButton("Make your move");
	  private JTextArea textArea = null;
	  private JLabel lblWin = null;
  	  private JTextField fldRow = new JTextField("");
  	  private JTextField fldCol = new JTextField("");	  
	  private ComputerPlayer computerPlayer;
	  private int height, width;
	  // constructor
	  public TextAreaView(TicTacToeGame TicTacToeGame, int width, int height) {
		    // import the game
			theGame = TicTacToeGame;
			// set dimensions
			this.height = height;
			this.width = width;
			// import our computer player
			computerPlayer = theGame.getComputerPlayer();
			// Initialize the textAreaPane;
			initializeTextAreaPanel();
	  }
	  // This method is called by OurObservable's notifyObservers()
	  public void update() {
		  	// occurs when a new game call is issued
		  	if (theGame.maxMovesRemaining() == theGame.size() * theGame.size()) {
		  		// create a new game
				textArea.setText(theGame.toString());
		    	// reset the button and label
				setLabelAndButtonState("Make your move", true);
		  	}
		  	// check if the game is still running
		    if (!theGame.stillRunning()) {
		    	// don't allow interaction
		    	stateButton.setEnabled(false);
	  		} else {
		    	 // update the board
		    	updateBoard();
		    	// reset the button and label
		    	setLabelAndButtonState("Make your move", true);
		    }
		    // always clear the fields
		    clearFields();
	  }
	  // update the board
	  public void updateBoard() {
			// write the game out 
			textArea.setText(theGame.toString());
	  }
	  // clear fields
	  public void clearFields() {
			// clear fields
			fldRow.setText("");
			fldCol.setText("");		  
	  }
	  // set label and button states
	  public void setLabelAndButtonState(String label, boolean state) {
		  	// set the label text
			lblWin.setText(label);
			// set the button text
			stateButton.setText(label);
			// set the enabled/disabled state of the button
			stateButton.setEnabled(state);			  
	  }
	  // Initialize the TextArea
	  public void initializeTextAreaPanel() {
		  	// variables
		  	JPanel input = new JPanel();
		  	JPanel button = new JPanel();
		  	JPanel topPanel = new JPanel();
		  	JPanel btmPanel = new JPanel();
		  	JPanel lblPanel = new JPanel();
			int size = theGame.size();
			// nested layout
			input.setLayout(new GridLayout(2, 2, 5, 5));
			button.setLayout(new GridLayout(1,1,10,10));
			topPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
			btmPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			lblPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			// set the background color to CYAN
			input.setBackground(Color.CYAN);
			button.setBackground(Color.CYAN);
			topPanel.setBackground(Color.CYAN);
			btmPanel.setBackground(Color.CYAN);
			lblPanel.setBackground(Color.CYAN);
			this.setBackground(Color.CYAN);
			// setup the font to use
			Font myFont = new Font("Courier", Font.BOLD, 42);
			// set text area
			textArea = new JTextArea(size, size);
			// set font for textArea
			textArea.setFont(myFont);
			// set the new empty game
			textArea.setText(theGame.toString());
			// not editable
			textArea.setEditable(false);
	    	// labels
	    	JLabel lblRow = new JLabel("row");
	    	JLabel lblCol = new JLabel("column");
	    	// set class label
	    	lblWin = new JLabel("Make Your Move");
	    	// set the button	
	        stateButton.setSize(100, 40);
	        stateButton.setFont(new Font("Courier", Font.BOLD, 12));
	        stateButton.setEnabled(true);
	        stateButton.setBackground(Color.PINK);
	        // add a listener
	        stateButton.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			// get row and column move
	    			int row, col;
	    			// check the input
	    			try {
		    			row = Integer.parseInt(fldRow.getText());
		    			col = Integer.parseInt(fldCol.getText());	    				
	    			} catch (Exception ex) {
	    				// tell them that the input is invalid
	    				JOptionPane.showMessageDialog(null, "Invalid input");
	    				// clear fields
		    			clearFields();
		    			// return
		    			return;	    				
	    			}
	    			// check if the move is valid
	    			if (row < 0 || row > (size - 1) || col < 0 || col > (size - 1)) {
	    				// tell them that this is an invalid move
	    				JOptionPane.showMessageDialog(null, "Invalid move");
	    				// clear fields
	    				clearFields();
		    			// return
		    			return;
	    			}
	    			// check availability
	    			if (theGame.available(row, col)) {
	    				// make the move
	    				theGame.choose(row, col);
	    				// check game state and update the notification label if necessary
	    				if (theGame.tied()) {
	    					// the game is tied.
	    					setLabelAndButtonState("Tied", false);
	    				} else if (theGame.didWin('X')) {
	    					// x wins
	    					setLabelAndButtonState("X Wins", false);    					
	    				} else {
	    					// allow the computer to make a move
	    					// get a point from our strategy
	    					Point play = computerPlayer.desiredMove(theGame);
	    					// play the move
	    					theGame.choose(play.x, play.y);
	    					// check if the computer won
	    					if (theGame.didWin('O')) {
	    						// o wins
	    						setLabelAndButtonState("O Wins", false); 						
	    					}
	    				}
	    				// update the board
	    				updateBoard();
	    			} else {
	    				// space is occupied
	    				JOptionPane.showMessageDialog(null, "Move not available");
	    				// clear fields
	    				clearFields();
		    			// return
		    			return;	    				
	    			}
	    			// clear fields
	    			clearFields();
	    		}
	    	}); 
	        // add fields and labels to the input panel
	    	input.add(fldRow);
	    	input.add(lblRow);
	    	input.add(fldCol);
	    	input.add(lblCol);
	    	// add button to the button panel
	    	button.add(stateButton);
	    	// add the form fields and button to the top panel
	    	topPanel.add(input);
	    	topPanel.add(button);
	    	// add the text area
	    	btmPanel.add(textArea);
	    	// add label
	    	lblPanel.add(lblWin);
	    	// clear out the layout
	    	this.setLayout(null);
	    	// set the location  and size
	    	topPanel.setLocation(10, 5);
	    	topPanel.setSize(width - 35, (height / 4));
	    	btmPanel.setLocation(10, (height / 3) - 40);
	    	btmPanel.setSize(width - 35, (height / 2));
	    	lblPanel.setLocation(10, height - height/4);
	    	lblPanel.setSize(width - 35, (height / 2));
	    	// add the top and bottom panels
	    	this.add(topPanel);
	    	this.add(btmPanel);
	    	this.add(lblPanel);
	  }
}
