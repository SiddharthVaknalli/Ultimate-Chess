import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 * Main Frame for a Chess Game sets up the menus and places a Board in the Frame
 * 
 * @author Sean Marchand and Siddharth Vaknalli
 * @version 19/01/2015
 * 
 */
public class ChessMain extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	// A lot of menu items.
	private Board chessBoard;
	private JMenuItem newMenuItem, statisticsOption, quitMenuItem;
	private JMenuItem undoOption, drawOption, aboutMenuItem, instructionsItem;
	private JMenuItem pawnHelp, knightHelp, bishopHelp, rookHelp, queenHelp,
			kingHelp, enPassantHelp, promotionHelp, castlingHelp;
	protected static boolean autoComplete = true;

	/**
	 * Constructs the Chess game and the Menu items
	 */
	public ChessMain()
	{
		// Basic setup for the Board
		super("Chess");
		setResizable(false);
		setLocation(200, 50);
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic('G');
		// Sets up the new game button
		newMenuItem = new JMenuItem("New Game");
		newMenuItem.addActionListener(this);
		// Sets up the Statistics button
		statisticsOption = new JMenuItem("Statistics");
		statisticsOption.addActionListener(this);
		// Sets up the exit option
		quitMenuItem = new JMenuItem("Exit");
		quitMenuItem.addActionListener(this);
		// Sets up undo option
		undoOption = new JMenuItem("Undo Move");
		undoOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				InputEvent.CTRL_MASK));
		undoOption.addActionListener(this);
		undoOption.setEnabled(false);
		// Sets up the declare draw option
		drawOption = new JMenuItem("Declare Draw");
		drawOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
				InputEvent.CTRL_MASK));
		drawOption.addActionListener(this);
		drawOption.setEnabled(false);
		gameMenu.add(newMenuItem);
		gameMenu.add(statisticsOption);
		gameMenu.add(undoOption);
		gameMenu.add(drawOption);
		gameMenu.addSeparator();
		gameMenu.add(quitMenuItem);
		menuBar.add(gameMenu);
		gameMenu.addSeparator();
		// Sets up the Help menus
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');
		aboutMenuItem = new JMenuItem("About...");
		aboutMenuItem.addActionListener(this);
		instructionsItem = new JMenuItem("How To Play...");
		instructionsItem.addActionListener(this);
		pawnHelp = new JMenuItem("The Pawn");
		pawnHelp.addActionListener(this);
		knightHelp = new JMenuItem("The Knight");
		knightHelp.addActionListener(this);
		bishopHelp = new JMenuItem("The Bishop");
		bishopHelp.addActionListener(this);
		rookHelp = new JMenuItem("The Rook");
		rookHelp.addActionListener(this);
		queenHelp = new JMenuItem("The Queen");
		queenHelp.addActionListener(this);
		kingHelp = new JMenuItem("The King");
		kingHelp.addActionListener(this);
		enPassantHelp = new JMenuItem("En Passant");
		enPassantHelp.addActionListener(this);
		promotionHelp = new JMenuItem("Promotion");
		promotionHelp.addActionListener(this);
		castlingHelp = new JMenuItem("Castling");
		castlingHelp.addActionListener(this);
		helpMenu.add(aboutMenuItem);
		helpMenu.add(instructionsItem);
		helpMenu.addSeparator();
		helpMenu.add(pawnHelp);
		helpMenu.add(knightHelp);
		helpMenu.add(bishopHelp);
		helpMenu.add(rookHelp);
		helpMenu.add(queenHelp);
		helpMenu.add(kingHelp);
		helpMenu.add(enPassantHelp);
		helpMenu.add(promotionHelp);
		helpMenu.add(castlingHelp);
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);
		// Set the name of the frame by calling the super constructor and
		// disable the option to resize it

		// Set the icon image of this ChessMain frame
		setIconImage(new ImageIcon("Temp\\King1.png").getImage());

		// Create and add the Board to a Container object
		chessBoard = new Board(this);
		Container contentPane = getContentPane();
		contentPane.add(chessBoard, BorderLayout.CENTER);
	}

	/**
	 * Handles menu events depending on what the user selects
	 * 
	 * @param event The type of event that occurred
	 */
	public void actionPerformed(ActionEvent event)
	{
		// Starts a new game
		if (event.getSource() == newMenuItem)
		{
			chessBoard.newGame();
			// Displays statistics
		}
		else if (event.getSource() == statisticsOption)
		{
			Statistics myStats = Statistics.readFromFile("stats.dat");
			JOptionPane.showMessageDialog(chessBoard,
					"Number of wins " + myStats.numWins
							+ "\n Number of Games played " + myStats.numGames
							+ "\n Win Percentage " + myStats.winPercent(),
					"Statistics", JOptionPane.INFORMATION_MESSAGE);
			// Quits the game
		}
		else if (event.getSource() == quitMenuItem)
		{
			System.exit(0);
			// Undoes a move
		}
		else if (event.getSource() == undoOption)
		{
			chessBoard.undo();
			if (!chessBoard.canUndo())
				setUndoOption(false);
			// Draws if the game is a tie
		}
		else if (event.getSource() == drawOption)
		{
			chessBoard.drawGame();
			if (chessBoard.canDrawGame())
				setDrawOption(true);
		}
		else if (event.getSource() == aboutMenuItem)
		{
			JOptionPane
					.showMessageDialog(
							chessBoard,
							"Chess by Siddharth Vaknalli\nand Sean Marchand\n\u00a9 2014/2015",
							"About Chess", JOptionPane.INFORMATION_MESSAGE);
			// Teaches the User how to play the game of Chess
		}
		else if (event.getSource() == instructionsItem)
		{
			JOptionPane
					.showMessageDialog(
							chessBoard,
							"Click on a piece to select it \nTo move the piece, click a highlighted square\nTo capture a piece, click a red highlighted square",
							"How To Play Chess",
							JOptionPane.INFORMATION_MESSAGE);
		}
		else if (event.getSource() == pawnHelp)
		{
			JOptionPane
					.showMessageDialog(
							chessBoard,
							"The Pawn has a few choice moves in chess. "
									+ "\nThe first is the basic move, the pawn can move forward "
									+ "\nby one space at any point in the game. "
									+ "\nThe pawn cannot capture by moving forward however,"
									+ "\nto capture with a pawn you must move diagonally forward one space, in either direction."
									+ "\nThis cannot be done except when capturing. "
									+ "\nThe pawn can also move forward two spaces if it has not moved from its starting position."
									+ "\nThe pawn is worth one point.",
							"How To Use The Pawn",
							JOptionPane.INFORMATION_MESSAGE);
		}
		else if (event.getSource() == knightHelp)
		{
			JOptionPane
					.showMessageDialog(
							chessBoard,
							"The knight has only one type of move, an L shape."
									+ "\nThe knight can move to a square that is two squares horizontally and one square vertically, or two squares vertically and one square horizontally. "
									+ "\nThe knight can also jump over other pieces, unlike every other piece in chess which cannot go through pieces."
									+ "\nThe knight captures by landing on an opposing piece in its regular move. "
									+ "\nThe knight is worth 3 points.",
							"How To Use The Knight",
							JOptionPane.INFORMATION_MESSAGE);

		}
		else if (event.getSource() == bishopHelp)
		{
			JOptionPane
					.showMessageDialog(
							chessBoard,
							"The bishop has no distance restrictions, but it can only move diagonally. "
									+ "\nThe bishop captures by landing on an opposing piece in its regular move. "
									+ "\nThe bishop is worth 3 points.",
							"How To Use The Bishop",
							JOptionPane.INFORMATION_MESSAGE);
		}
		else if (event.getSource() == rookHelp)
		{
			JOptionPane
					.showMessageDialog(
							chessBoard,
							"The rook has no distance restrictions, but it can only move straight, either forward or sideways. "
									+ "\nThe rook captures by landing on an opposing piece in its regular move. "
									+ "\nThe rook is worth 5 points.",
							"How To Use The Rook",
							JOptionPane.INFORMATION_MESSAGE);
		}
		else if (event.getSource() == queenHelp)
		{
			JOptionPane
					.showMessageDialog(
							chessBoard,
							"The queen is essentially a combination of the rook and bishop."
									+ "\nThe queen can move diagonally, forward or sideways and there is no distance limit. "
									+ "\nThe queen is worth nine points.",
							"How To Use The Queen",
							JOptionPane.INFORMATION_MESSAGE);
		}
		else if (event.getSource() == kingHelp)
		{
			JOptionPane
					.showMessageDialog(
							chessBoard,
							" The king can only move one space in any direction. "
									+ "\nThe king is considered to be in check when it could be taken by an opponent's piece in the next turn. "
									+ "\nThe king cannot move into check, and you cannot move another piece so that your king would be in check. "
									+ "\nYou lose the game when your king is in checkmate. "
									+ "\nCheckmate occurs when there is nowhere to move your king or other pieces to stop your king form being taken.",
							"How To Use The King",
							JOptionPane.INFORMATION_MESSAGE);

		}
		else if (event.getSource() == enPassantHelp)
		{
			JOptionPane
					.showMessageDialog(
							chessBoard,
							"En Passant is a special rule in chess that only applies to pawns.\n"
									+ "It specifies that if an opponent's pawn moves forward twice and lands parallel to a pawn under your control.\n"
									+ "You can take the pawn in the same way as you would normally do; diagonally forward one space. \n"
									+ "The option to take with En Passant expires after one turn.",
							"Explaining En Passant",
							JOptionPane.INFORMATION_MESSAGE);
		}

		else if (event.getSource() == promotionHelp)
		{
			JOptionPane
					.showMessageDialog(
							chessBoard,
							"Promotion is a special bonus you can get for getting a pawn to the opponents end of the board.\n"
									+ "If a pawn under your control successfully gets to the opponents end, you can trade the pawn in for either a:\n"
									+ "Queen, Rook, Knight, or Bishop. You can do this as many times as you have pawns.",
							"Explaining Promotion",
							JOptionPane.INFORMATION_MESSAGE);
		}
		// I'd like to thank Wikipedia for this explanation
		else if (event.getSource() == castlingHelp)
		{
			JOptionPane
					.showMessageDialog(
							chessBoard,
							"Castling is very important in the game of chess.\n"
									+ "It allows players to both defend their King and get their Rook into the action.\n"
									+ "Castling consists of moving the king two squares towards a rook on the player's first rank, \n"
									+ "then moving the rook to the square over which the king crossed\n"
									+ "Castling can only be done if the king has never moved, the rook involved has never moved, \n"
									+ "the squares between the king and the rook involved are unoccupied, the king is not in check, \n"
									+ "and the king does not cross over or end on a square in which it would be in check\n"
									+ "Castling is a King move; not a rook.",
							"Explaining Castling",
							JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Sets the Undo object in the Menu
	 * 
	 * @param canUndo if you can undo or not
	 */
	public void setUndoOption(boolean canUndo)
	{
		this.undoOption.setEnabled(canUndo);
	}

	/**
	 * Sets the Draw in the menu
	 * 
	 * @param canDraw if you can draw or not
	 */
	public void setDrawOption(boolean canDraw)
	{
		this.drawOption.setEnabled(canDraw);
	}

	/**
	 * Sets up the Chess game
	 * 
	 * @param args Dummy parameter
	 */
	public static void main(String[] args)
	{
		ChessMain frame = new ChessMain();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
