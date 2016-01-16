import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Keeps track of a Board class for a game of Chess. It extends the JPanel
 * class, and inherits all methods and data from it. Also implements
 * MouseListener and MouseMotionListener interfaces to listen and respond to
 * specific mouse events. Keeps track of constant variables for the layout of
 * the JPanel, images and buttons for the different screens and panels, the
 * board and other variables for keeping track of the game as well as the
 * computer's moves if the user plays against the computer. Includes methods to
 * construct a Board object which initializes the board and all variables given
 * the JFrame class that contains it, to start a new game of chess, to check if
 * the board or a particular Square contains a Piece, to draw all the different
 * screens, panels and all the Squares and Pieces if a game is in progress. Also
 * contains methods to make a computer move if playing versus the computer, to
 * evaluate the board based on the positions of all Pieces on the board, and to
 * calculate the best possible moves that the computer can make according to the
 * color it plays by looking ahead of the user's moves. Further includes methods
 * to undo moves made in a game, to check if moves can be undone, to check if
 * the game can be drawn and to check if the current game has ended by win or
 * draw. Finally, includes methods to check where the mouse has been clicked or
 * moved and to respond accordingly
 * 
 * @author Siddharth Vaknalli and Sean Marchand
 * @version Wednesday, January 21, 2015
 */
public class Board extends JPanel implements MouseListener, MouseMotionListener
{
	/**
	 * The default serial version UID (to delete all warnings)
	 */
	private static final long serialVersionUID = 1L;

	// Constants for the panel layout
	public static final int BOARD_X = 625;
	public static final int BOARD_Y = 75;
	private static final int PANEL_WIDTH = 525;
	private static final int MESSAGEBAR_X = 110;
	private static final int MESSAGEBAR_Y = 125;
	public static final int WIDTH = 1250;
	public static final int HEIGHT = 700;
	private final Color BG_COLOR = new Color(156, 130, 82);
	private static final int ANIMATION_FRAMES = 5;
	private static int turn = 1;
	private static String[] endGameTypes = { "Checkmate", "Stalemate" };

	// Keep track of images for the screens and panels
	private final Image twoPlayerPanel = new ImageIcon(
			"Temp\\Twoplayerpanel.png")
			.getImage();
	private final Image vsComputerPanel0 = new ImageIcon(
			"Temp\\vsComputer0.png").getImage();
	private final Image vsComputerPanel1 = new ImageIcon(
			"Temp\\vsComputer1.png").getImage();
	private final Image frame = new ImageIcon("Temp\\Frame2.jpg").getImage();
	private final Image background = new ImageIcon("Temp\\background.jpg")
			.getImage();
	private final Image introImage = new ImageIcon("Temp\\Intro.png")
			.getImage();
	private final Image difficultyImage = new ImageIcon("Temp\\Difficulty.png")
			.getImage();
	private final Image colorChoiceImage = new ImageIcon(
			"Temp\\Colorchoice.png").getImage();

	// Rectangle variables for buttons on the different screens
	private Rectangle vsComputerButton = new Rectangle(445, 400, 476, 40);
	private Rectangle twoPlayerButton = new Rectangle(494, 507, 379, 40);
	private Rectangle easyButton = new Rectangle(583, 375, 117, 40);
	private Rectangle mediumButton = new Rectangle(533, 455, 217, 40);
	private Rectangle hardButton = new Rectangle(573, 531, 137, 40);
	private Rectangle veryHardButton = new Rectangle(500, 606, 280, 40);
	private Rectangle whiteButton = new Rectangle(558, 474, 159, 40);
	private Rectangle blackButton = new Rectangle(570, 590, 150, 40);
	private Rectangle undoButton = new Rectangle(105, 605, 70, 25);
	private Rectangle newGameButton = new Rectangle(285, 605, 145, 25);
	private Rectangle statisticsButton = new Rectangle(80, 650, 120, 30);
	private Rectangle mainMenuButton = new Rectangle(257, 650, 203, 30);

	// Variables to keep track of the screen
	private boolean mainScreen;
	private boolean difficultyScreen;
	private boolean colorChoiceScreen;
	private boolean vsComputerScreen;
	public static boolean twoPlayerScreen;

	// Variables for the chess game
	private ChessMain chessFrame;
	private Square[][] board;
	private Piece selectedPiece;
	private Square sourceSquare;
	private LinkedList<Move> moves;
	private ArrayList<Square> movesGenerated;
	private ArrayList<Piece> capturedPieces;
	private boolean gameOver;
	private String endGameType;
	private boolean moveUndone;
	private boolean drawCalled;

	// Variables for the computer AI component
	private Piece miniBestPiece;
	private Square miniBestSourceSquare;
	private Square miniBestMove;
	private Piece maxiBestPiece;
	private Square maxiBestSourceSquare;
	private Square maxiBestMove;
	private int aiDifficulty;
	private int playerColor;
	private boolean playerMoveMade;

	/**
	 * Constructs a new Board object with the given JFrame which contains it.
	 * Sets up the size and position of the JPanel so that it can be visible on
	 * the screen and enables it to respond to mouse events. Also initializes
	 * all the Squares on the chess board and other required variables for the
	 * game, such as the List of moves made, captured pieces. Also, sets the
	 * screen to the main/introduction screen
	 * @param frame
	 */
	public Board(ChessMain frame)
	{
		// Set the chess frame, and the size and background color of this panel
		frame.setSize(new Dimension(WIDTH, HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(BG_COLOR);
		chessFrame = frame;

		// Add mouse listeners to the Board
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		// Initialize all the Squares on the chess board
		board = new Square[8][8];

		board[0][0] = new Square(0, 0, 0, null);
		board[0][1] = new Square(0, 1, 1, null);
		board[0][2] = new Square(0, 2, 0, null);
		board[0][3] = new Square(0, 3, 1, null);
		board[0][4] = new Square(0, 4, 0, null);
		board[0][5] = new Square(0, 5, 1, null);
		board[0][6] = new Square(0, 6, 0, null);
		board[0][7] = new Square(0, 7, 1, null);

		int color = 1;
		for (int col = 0; col < board[0].length; col++)
		{
			board[1][col] = new Square(1, col, color, null);
			if (color == 1)
				color = 0;
			else
				color = 1;
		}

		color = 0;
		for (int row = 2; row <= 5; row++)
		{
			for (int col = 0; col <= 7; col++)
			{
				board[row][col] = new Square(row, col, color, null);
				if (color == 0)
					color = 1;
				else
					color = 0;
			}
			if (color == 0)
				color = 1;
			else
				color = 0;
		}

		color = 0;
		for (int col = 0; col < board[0].length; col++)
		{
			board[6][col] = new Square(6, col, color, null);
			if (color == 1)
				color = 0;
			else
				color = 1;
		}

		board[7][0] = new Square(7, 0, 1, null);
		board[7][1] = new Square(7, 1, 0, null);
		board[7][2] = new Square(7, 2, 1, null);
		board[7][3] = new Square(7, 3, 0, null);
		board[7][4] = new Square(7, 4, 1, null);
		board[7][5] = new Square(7, 5, 0, null);
		board[7][6] = new Square(7, 6, 1, null);
		board[7][7] = new Square(7, 7, 0, null);

		// Show the main screen when the game first runs and initialize the
		// lists of captured pieces and moves made
		mainScreen = true;
		vsComputerScreen = false;
		twoPlayerScreen = false;
		capturedPieces = new ArrayList<Piece>();
		moves = new LinkedList<Move>();
	}

	/**
	 * Starts a new game of Chess by removing all the pieces from the previous
	 * game and adding all pieces to their original positions on the board.
	 * Also, resets the lists of moves made, captured pieces, selected
	 * pieces/squares and other game variables
	 */
	public void newGame()
	{
		// Read the stored statistics of the previous games from a file
		Statistics.readFromFile("stats.dat").newGame();

		// White always plays first, so set the turn to 1. Also, clear the lists
		// of captured pieces and moves made
		turn = 1;
		moves.clear();
		capturedPieces.clear();

		// The game is not over, no player move has been made, a draw has not
		// been called and no move has been undone at the start of the game
		gameOver = false;
		playerMoveMade = false;
		endGameType = null;
		moveUndone = false;
		drawCalled = false;

		// Reset all the computer move variables (for the AI component)
		miniBestPiece = null;
		miniBestSourceSquare = null;
		miniBestMove = null;
		maxiBestPiece = null;
		maxiBestSourceSquare = null;
		maxiBestMove = null;

		// Since no moves has been made yet, the undo option of the JFrame must
		// be set to false
		chessFrame.setUndoOption(false);

		// Remove all the pieces from their positions in the previous game
		for (int row = 0; row < board.length; row++)
			for (int col = 0; col < board[row].length; col++)
				board[row][col].removePiece();

		// Add all the pieces to their initial positions on the board
		board[0][0].addPiece(new Rook(0, 0, 1), false);
		board[0][1].addPiece(new Knight(0, 1, 1), false);
		board[0][2].addPiece(new Bishop(0, 2, 1), false);
		board[0][3].addPiece(new Queen(0, 3, 1), false);
		board[0][4].addPiece(new King(0, 4, 1), false);
		board[0][5].addPiece(new Bishop(0, 5, 1), false);
		board[0][6].addPiece(new Knight(0, 6, 1), false);
		board[0][7].addPiece(new Rook(0, 7, 1), false);

		for (int col = 0; col < board[0].length; col++)
			board[1][col].addPiece(new Pawn(1, col, 1), false);

		for (int col = 0; col < board[0].length; col++)
			board[6][col].addPiece(new Pawn(6, col, 0), false);

		board[7][0].addPiece(new Rook(7, 0, 0), false);
		board[7][1].addPiece(new Knight(7, 1, 0), false);
		board[7][2].addPiece(new Bishop(7, 2, 0), false);
		board[7][3].addPiece(new Queen(7, 3, 0), false);
		board[7][4].addPiece(new King(7, 4, 0), false);
		board[7][5].addPiece(new Bishop(7, 5, 0), false);
		board[7][6].addPiece(new Knight(7, 6, 0), false);
		board[7][7].addPiece(new Rook(7, 7, 0), false);

		repaint();
	}

	/**
	 * Checks whether the given Point is contained in the chess board in this
	 * Panel
	 * 
	 * @param point the Point to check for
	 * @return true if the chess board contains the given Point, false otherwise
	 */
	public boolean containedInBoard(Point point)
	{
		// Return whether the underlining rectangle of the board contains the
		// given Point
		return new Rectangle(BOARD_X, BOARD_Y, 8 * Square.SIDE, 8 * Square.SIDE)
				.contains(point);
	}

	/**
	 * Gets the selected Square on the chess board given the point of selection
	 * 
	 * @param point the Point of selection Precondition: The Point exists within
	 *            the chess board in this Board
	 * @return the Square from the chess board which is selected
	 */
	public Square getSelectedSquare(Point point)
	{
		// Calculate the row and column and return the appropriate Square
		int row = (point.y - BOARD_Y) / Square.SIDE;
		int column = (point.x - BOARD_X) / Square.SIDE;
		return board[row][column];
	}

	/**
	 * Draws the current screen of the game and the appropriate images in it. If
	 * a game is currently under progress, also draws the side panel, the
	 * appropriate messages (check, check mate, draw etc), the captured pieces
	 * and the board along with all the pieces in their current positions
	 * @param g the Graphics object in the context of which the screen has to be
	 *            painted
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		// Draw the main screen image if the current screen is the main screen
		if (mainScreen)
			g.drawImage(introImage, 0, 0, WIDTH, HEIGHT, null);

		// Draw the choice of difficulty screen if chosen (if the user chooses
		// to play against the computer)
		else if (difficultyScreen)
			g.drawImage(difficultyImage, 0, 0, WIDTH, HEIGHT, null);

		// Draw the screen giving the user choice of color if playing against
		// the computer (after the difficulty screen)
		else if (colorChoiceScreen)
			g.drawImage(colorChoiceImage, 0, 0, WIDTH, HEIGHT, null);

		// If a game is being played versus the computer, draw the following
		else if (vsComputerScreen)
		{
			// Draw the background and side panel
			g.drawImage(background, 0, 0, WIDTH, HEIGHT, null);
			if (playerColor == 1)
				g.drawImage(vsComputerPanel1, 0, 0, PANEL_WIDTH, HEIGHT, null);
			else
				g.drawImage(vsComputerPanel0, 0, 0, PANEL_WIDTH, HEIGHT, null);
			g.drawImage(frame, BOARD_X - 60, BOARD_Y - 60,
					Square.SIDE * 8 + 120, Square.SIDE * 8 + 120, null);

			// Label and number the rows and columns as a chess board is
			// labeled
			Font font = new Font("Calibri", Font.BOLD, 30);
			g.setFont(font);
			g.setColor(Color.BLACK);
			int x = 650;
			int y = 55;
			for (char col = 'A'; col <= 'H'; col++)
			{
				g.drawString(Character.toString(col), x, y);
				x += Square.SIDE;
			}

			x = 650;
			y = 645;
			for (char col = 'A'; col <= 'H'; col++)
			{
				g.drawString(Character.toString(col), x, y);
				x += Square.SIDE;
			}

			x = 580;
			y = 120;
			for (int row = 1; row <= 8; row++)
			{
				g.drawString(Integer.toString(row), x, y);
				y += Square.SIDE;
			}

			x = 1175;
			y = 120;
			for (int row = 1; row <= 8; row++)
			{
				g.drawString(Integer.toString(row), x, y);
				y += Square.SIDE;
			}

			// Draw the chess board and its outline
			g.drawRect(BOARD_X, BOARD_Y, 8 * Square.SIDE, 8 * Square.SIDE);
			for (int row = 0; row < board.length; row++)
				for (int col = 0; col < board[row].length; col++)
					board[row][col].draw(g);

			// Set the font and its color for drawing
			font = new Font("Calibri", Font.BOLD, 40);
			g.setFont(font);
			// g.drawString("White", BOARD_X - Square.SIDE - 30, BOARD_Y - 80);
			// g.drawString("Pieces", BOARD_X - Square.SIDE - 30, BOARD_Y - 60);
			// g.drawString("Captured", BOARD_X - Square.SIDE - 45, BOARD_Y -
			// 40);
			g.setColor(Color.BLACK);
			// g.drawString("Black", BOARD_X + 430, BOARD_Y - 80);
			// g.drawString("Pieces", BOARD_X + 425, BOARD_Y - 60);
			// g.drawString("Captured", BOARD_X + 410, BOARD_Y - 40);

			// Check for the end of the current game if it is not drawn
			if (!drawCalled)
				checkEndOfGame();

			// Check if the game can be drawn and set the option in the JFrame
			// accordingly
			chessFrame.setDrawOption(canDrawGame());

			// Draw the appropriate message in the side panel, according to the
			// current status of the game. Display the appropriate message if a
			// king is in check if the game is not over. Also, display whether
			// the computer is thinking to make a move after the player has made
			// a move. Also, display turns if possible
			if (!gameOver && playerMoveMade)
				g.drawString("Computer thinking...",
						MESSAGEBAR_X, MESSAGEBAR_Y);
			else if (!gameOver)
			{
				boolean kingInCheck = false;
				for (int row = 0; row < board.length; row++)
					for (int col = 0; col < board[row].length; col++)
					{
						Square square = board[row][col];
						if (square.containsPiece()
								&& square.piece instanceof King)
						{
							King king = (King) square.piece;
							if (king.inCheck())
							{
								if (king.color == 0)
									g.drawString("White King in Check!",
											MESSAGEBAR_X, MESSAGEBAR_Y);
								else
									g.drawString("Black King in Check!",
											MESSAGEBAR_X, MESSAGEBAR_Y);
								kingInCheck = true;
							}
						}
					}

				if (!kingInCheck && turn == 1)
					g.drawString("White's turn",
							MESSAGEBAR_X + 40, MESSAGEBAR_Y);
				else if (!kingInCheck && turn == -1)
					g.drawString("Black's turn",
							MESSAGEBAR_X + 40, MESSAGEBAR_Y);
			}

			// If the game is over, display the appropriate message
			else if (endGameType != null)
			{
				g.drawString(endGameType + "!", MESSAGEBAR_X + 40, MESSAGEBAR_Y);
				if (turn == 1)
					Statistics.readFromFile("stats.dat").win();
			}

			// Draw all the captured pieces
			for (Piece captured : capturedPieces)
				captured.draw(g);

		}
		else if (twoPlayerScreen)
		{
			// Draw the background and side panels
			g.drawImage(background, 0, 0, WIDTH, HEIGHT, null);
			g.drawImage(twoPlayerPanel, 0, 0, PANEL_WIDTH, HEIGHT, null);
			g.drawImage(frame, BOARD_X - 60, BOARD_Y - 60,
					Square.SIDE * 8 + 120, Square.SIDE * 8 + 120, null);

			// Label and number the rows and columns
			Font font = new Font("Calibri", Font.BOLD, 30);
			g.setFont(font);
			g.setColor(Color.BLACK);
			int x = 650;
			int y = 55;
			for (char col = 'A'; col <= 'H'; col++)
			{
				g.drawString(Character.toString(col), x, y);
				x += Square.SIDE;
			}

			x = 650;
			y = 645;
			for (char col = 'A'; col <= 'H'; col++)
			{
				g.drawString(Character.toString(col), x, y);
				x += Square.SIDE;
			}

			x = 580;
			y = 120;
			for (int row = 1; row <= 8; row++)
			{
				g.drawString(Integer.toString(row), x, y);
				y += Square.SIDE;
			}

			x = 1175;
			y = 120;
			for (int row = 1; row <= 8; row++)
			{
				g.drawString(Integer.toString(row), x, y);
				y += Square.SIDE;
			}

			// Draw the chess board and its outline
			g.drawRect(BOARD_X, BOARD_Y, 8 * Square.SIDE, 8 * Square.SIDE);
			for (int row = 0; row < board.length; row++)
				for (int col = 0; col < board[row].length; col++)
					board[row][col].draw(g);

			// Set the font and color for drawing
			font = new Font("Calibri", Font.BOLD, 40);
			g.setFont(font);
			g.setColor(Color.BLACK);

			// Draw the headings for captured Pieces
			// g.drawString("White", BOARD_X - Square.SIDE - 30, BOARD_Y - 80);
			// g.drawString("Pieces", BOARD_X - Square.SIDE - 30, BOARD_Y - 60);
			// g.drawString("Captured", BOARD_X - Square.SIDE - 45, BOARD_Y -
			// 40);
			// g.drawString("Black", BOARD_X + 430, BOARD_Y - 80);
			// g.drawString("Pieces", BOARD_X + 425, BOARD_Y - 60);
			// g.drawString("Captured", BOARD_X + 410, BOARD_Y - 40);

			// Check for the end of the current game if it is not drawn
			if (!drawCalled)
				checkEndOfGame();

			// Check if the game can be drawn and set the option in the JFrame
			// accordingly
			chessFrame.setDrawOption(canDrawGame());

			// Display the appropriate message if a king is in check if the game
			// is not over. Also, display whose turn it is (if possible)
			if (!gameOver)
			{
				boolean kingInCheck = false;
				for (int row = 0; row < board.length; row++)
					for (int col = 0; col < board[row].length; col++)
					{
						Square square = board[row][col];
						if (square.containsPiece()
								&& square.piece instanceof King)
						{
							King king = (King) square.piece;
							if (king.inCheck())
							{
								if (king.color == 0)
									g.drawString("White King in Check!",
											MESSAGEBAR_X, MESSAGEBAR_Y);
								else
									g.drawString("Black King in Check!",
											MESSAGEBAR_X, MESSAGEBAR_Y);
								kingInCheck = true;
							}
						}
					}

				if (!kingInCheck && turn == 1)
					g.drawString("White's turn",
							MESSAGEBAR_X + 40, MESSAGEBAR_Y);
				else if (!kingInCheck && turn == -1)
					g.drawString("Black's turn",
							MESSAGEBAR_X + 40, MESSAGEBAR_Y);
			}

			// If the game is over, display the appropriate message
			else if (endGameType != null)
			{
				g.drawString(endGameType + "!", MESSAGEBAR_X + 40, MESSAGEBAR_Y);

				// Store the statistics in a file if the game is over
				if (turn == 1)
					Statistics.readFromFile("stats.dat").win();
			}

			// Draw all the captured pieces
			for (Piece captured : capturedPieces)
				captured.draw(g);
		}

	}

	/**
	 * Delays the given number of milliseconds
	 * 
	 * @param milliSec number of milliseconds to delay
	 */
	private void delay(int milliSec)
	{
		try
		{
			Thread.sleep(milliSec);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * Makes a move of the computer when the user plays against the computer.
	 * Moves the given piece from the original Square to the Square it is
	 * supposed to be moved to. Also handles checking and un-checking kings,
	 * castling and pawn promotion when the piece is moved
	 * @param pieceToMove the computer Piece to move
	 * @param from the original Square to move the Piece from
	 * @param to the Square to move the Piece to Postcondition: The pieces on
	 *            the board are not in their original positions
	 */
	public void makeComputerMove(Piece pieceToMove, Square from, Square to)
	{
		// Remove the Piece to move from the original Square and add it to the
		// Square to move to. Also, add any captured piece to the list of
		// captured pieces
		from.removePiece();
		Piece capturedPiece = to.addPiece(pieceToMove,
				false);
		if (capturedPiece != null)
		{
			capturedPiece.capture(capturedPieces);
			capturedPieces.add(capturedPiece);
		}

		// If the Piece is a King and moves two spaces, it must be castling.
		// Move the Rook the King is castling with accordingly
		if (pieceToMove instanceof King
				&& Math.abs(from.column
						- to.column) > 1)
		{
			// Keep track of the Rook being castled with
			Rook rook;

			// Handle castling on the King side
			if (to.column == 6
					&& board[to.row][to.column + 1].piece instanceof Rook)
			{
				rook = (Rook) board[to.row][to.column + 1].piece;
				board[rook.row][rook.column].removePiece();
				board[to.row][to.column - 1].addPiece(
						rook, false);
				rook.castle(to.row, to.column + 1);
			}

			// Handle castling on the Queen side
			else if (to.column == 2
					&& board[to.row][to.column - 2].piece instanceof Rook)
			{
				rook = (Rook) board[to.row][to.column - 2].piece;
				board[rook.row][rook.column].removePiece();
				board[to.row][to.column + 1].addPiece(
						rook, false);
				rook.castle(to.row, to.column - 2);
			}

		}

		// If the piece being moved is a Pawn and is being moved to the back row
		// of the opposite side, promote it to a Queen
		if (pieceToMove instanceof Pawn && to.row == 7)
		{
			to.removePiece();
			to.addPiece(new Queen(to.row,
					to.column, 1), false);
		}

		// Generate further possible moves after moving the Piece to
		// the Square to check if it checks the opponent King. If it does, check
		// the opponent King
		ArrayList<Square> nextMovesPossible = to.piece
				.generateMoves(board);
		for (Square movePossible : nextMovesPossible)
			if (movePossible.containsOpponentKing(to.piece))
			{
				King kingToCheck = (King) movePossible.piece;
				kingToCheck.check(to);
			}

		// Un-check each King if it is no longer in threat (handled
		// in the uncheck () method in each King)
		int kingsChecked = 0;
		for (int row = 0; row < board.length && kingsChecked < 2; row++)
			for (int col = 0; col < board[0].length
					&& kingsChecked < 2; col++)
			{
				Square square = board[row][col];
				if (square.containsPiece())
				{
					Piece piece = square.piece;
					if (piece instanceof King)
					{
						King king = (King) piece;
						king.uncheck(board);
						kingsChecked++;
					}
				}
			}

		// Add the move made to the List of moves made
		moves.addLast(new Move(from, to,
				pieceToMove, capturedPiece));

		// Set the piece and the two squares to null
		pieceToMove = null;
		from = null;
		to = null;
	}

	/**
	 * Moves a Piece from the given Point to the given Point with animation
	 * 
	 * @param pieceToMove the Piece being moved
	 * @param fromPos initial position of the Piece
	 * @param toPos final position of the Piece
	 */
	public void moveAPiece(final Piece pieceToMove, Point fromPos, Point toPos)
	{
		// Keep track of the amount to change the position by each time
		// according to the animation frames
		int dx = (toPos.x - fromPos.x) / ANIMATION_FRAMES;
		int dy = (toPos.y - fromPos.y) / ANIMATION_FRAMES;

		// Move the position of the Piece by the above amount and repaint
		// immediately to animate it
		for (int times = 1; times <= ANIMATION_FRAMES; times++)
		{
			fromPos.x += dx;
			fromPos.y += dy;
			pieceToMove.setPosition(fromPos);
			paintImmediately(0, 0, getWidth(), getHeight());
			delay(30);
		}

		// Set the position of this Piece to the given position
		pieceToMove.setPosition(toPos);
	}

	/**
	 * Makes the computer play when the user plays against the computer
	 * according to the color and difficulty chosen by the user, after looking
	 * ahead of the user's moves
	 */
	public void playComputer()
	{
		// long startTime = System.nanoTime();

		// If the computer plays as black, look ahead of the user's moves and
		// find the best possible move that can be made that minimizes the score
		// of the board. Then, make the appropriate move
		if (turn == -1)
		{
			alphaBetaMin(Integer.MIN_VALUE, Integer.MAX_VALUE, aiDifficulty);
			makeComputerMove(miniBestPiece, miniBestSourceSquare, miniBestMove);
		}

		// If the computer plays as white, look ahead of the user's moves and
		// find the best possible move that can be made that maximizes the score
		// of the board. Then, make the appropriate move
		else
		{
			alphaBetaMax(Integer.MIN_VALUE, Integer.MAX_VALUE, aiDifficulty);
			makeComputerMove(maxiBestPiece, maxiBestSourceSquare, maxiBestMove);
		}

		// long endTime = System.nanoTime();
		// System.out.printf("%nTime for computer move: %.3f s",
		// (endTime - startTime) / 1000000000.0);

		// Set the turn to the user
		turn *= -1;

		repaint();

		playerMoveMade = false;
	}

	public int evaluate()
	{
		/*
		 * // Create a copy of the current state of the board before making all
		 * the // look ahead moves Square[][] currentBoard = new Square[8][8];
		 * for (int i = 0; i < board.length; i++) System.arraycopy(board[i], 0,
		 * currentBoard[i], 0, board[i].length);
		 * 
		 * for (Move move : lookAheadMoves) { Square moveTo =
		 * currentBoard[move.getMoveSquare().row][move .getMoveSquare().column];
		 * Square source = currentBoard[move.getSourceSquare().row][move
		 * .getSourceSquare().column]; Piece moved = move.getPieceMoved();
		 * 
		 * Piece removedPiece = moveTo.addPiece(source.removePiece());
		 * 
		 * // Generate further possible moves after moving the Piece to // the
		 * selected Square to check if it checks the opponent // King. If it
		 * does, check the opponent King ArrayList<Square> nextMovesPossible =
		 * moveTo.piece .generateMoves(currentBoard); for (Square movePossible :
		 * nextMovesPossible) if
		 * (movePossible.containsOpponentKing(moveTo.piece)) { King kingToCheck
		 * = (King) movePossible.piece; kingToCheck.check(moveTo); }
		 * 
		 * // Uncheck each King if it is no longer in threat (handled // in the
		 * uncheck () method in each King) int kingsChecked = 0; for (int row =
		 * 0; row < currentBoard.length && kingsChecked < 2; row++) for (int col
		 * = 0; col < currentBoard[0].length && kingsChecked < 2; col++) {
		 * Square square = currentBoard[row][col]; if (square.containsPiece()) {
		 * Piece piece = square.piece; if (piece instanceof King) { King king =
		 * (King) piece; king.uncheck(currentBoard); kingsChecked++; } }
		 * 
		 * }
		 * 
		 * }
		 */

		int score = 0;
		for (int row = 0; row < board.length; row++)
		{
			for (int col = 0; col < board[0].length; col++)
			{
				if (board[row][col].containsPiece())
				{
					if (board[row][col].piece.color == 0)
					{
						score += board[row][col].piece.getValue();
					}
					else
						score -= board[row][col].piece.getValue();
				}
			}
		}

		if (endGameType == endGameTypes[0] && turn == -1)
			score = -100000;
		else if (endGameType == endGameTypes[0] && turn == 1)
			score = 100000;

		return score;

	}

	public int alphaBetaMax(int alpha, int beta, int lookAheadDepth)
	{
		if (lookAheadDepth == 0)
			return evaluate();

		// int maxScore = Integer.MIN_VALUE;

		Piece bestPiece = null;
		Square bestSourceSquare = null;
		Square bestMove = null;

		for (int row = 0; row < board.length; row++)
			for (int col = 0; col < board[row].length; col++)
			{
				Square square = board[row][col];
				if (square.containsPiece() && square.piece.color == 0)
				{
					ArrayList<Square> movesPossible = square.piece
							.generateMoves(board);
					handleChecks(movesPossible, square, square.piece);
					for (Square move : movesPossible)
					{
						King kingChecked = null;

						Piece removedPiece = move
								.addPiece(square.removePiece(), true);

						// Generate further possible moves after moving the
						// Piece to
						// the selected Square to check if it checks the
						// opponent
						// King. If it does, check the opponent King
						ArrayList<Square> nextMovesPossible = move.piece
								.generateMoves(board);
						for (Square movePossible : nextMovesPossible)
							if (movePossible
									.containsOpponentKing(move.piece))
							{
								King kingToCheck = (King) movePossible.piece;
								kingToCheck.check(move);
								kingChecked = kingToCheck;
							}

						// Uncheck each King if it is no longer in threat
						// (handled
						// in the uncheck () method in each King)
						int kingsChecked = 0;
						for (int r = 0; r < board.length && kingsChecked < 2; r++)
							for (int c = 0; c < board[0].length
									&& kingsChecked < 2; c++)
							{
								Square sq = board[r][c];
								if (sq.containsPiece())
								{
									Piece piece = sq.piece;
									if (piece instanceof King)
									{
										King king = (King) piece;
										king.uncheck(board);
										kingsChecked++;
									}
								}
							}

						int score = alphaBetaMin(alpha, beta,
								lookAheadDepth - 1);

						// lookAheadMoves.remove(lookAheadMoves.size() - 1);

						/*
						 * if (score > maxScore) { maxScore = score; bestPiece =
						 * move.piece; bestSourceSquare = square; bestMove =
						 * move; }
						 */

						if (score >= beta)
						{
							square.addPiece(move.removePiece(), true);
							if (removedPiece != null)
								move.addPiece(removedPiece, true);

							if (kingChecked != null)
								kingChecked.uncheck(board);

							ArrayList<Square> nextMoves = square.piece
									.generateMoves(board);
							for (Square movePossible : nextMoves)
								if (movePossible
										.containsOpponentKing(square.piece))
								{
									King kingToCheck = (King) movePossible.piece;
									kingToCheck.check(move);
								}

							this.maxiBestPiece = square.piece;
							this.maxiBestSourceSquare = square;
							this.maxiBestMove = move;
							return beta;
						}
						if (score > alpha)
						{
							alpha = score;
							bestPiece = move.piece;
							bestSourceSquare = square;
							bestMove = move;
						}

						square.addPiece(move.removePiece(), true);
						if (removedPiece != null)
							move.addPiece(removedPiece, true);

						if (kingChecked != null)
							kingChecked.uncheck(board);

						ArrayList<Square> nextMoves = square.piece
								.generateMoves(board);
						for (Square movePossible : nextMoves)
							if (movePossible.containsOpponentKing(square.piece))
							{
								King kingToCheck = (King) movePossible.piece;
								kingToCheck.check(move);
							}
					}
				}
			}

		this.maxiBestPiece = bestPiece;
		this.maxiBestSourceSquare = bestSourceSquare;
		this.maxiBestMove = bestMove;

		return alpha;

	}

	public int alphaBetaMin(int alpha, int beta, int lookAheadDepth)
	{
		if (lookAheadDepth == 0)
			return evaluate();

		// int minScore = Integer.MAX_VALUE;

		Piece bestPiece = null;
		Square bestSourceSquare = null;
		Square bestMove = null;

		for (int row = 0; row < board.length; row++)
			for (int col = 0; col < board[row].length; col++)
			{
				Square square = board[row][col];
				if (square.containsPiece() && square.piece.color == 1)
				{
					ArrayList<Square> movesPossible = square.piece
							.generateMoves(board);
					handleChecks(movesPossible, square, square.piece);

					for (Square move : movesPossible)
					{
						// lookAheadMoves.add(new Move(square, move,
						// square.piece,
						// move.piece));
						King kingChecked = null;

						Piece removedPiece = move
								.addPiece(square.removePiece(), true);

						// Generate further possible moves after moving the
						// Piece to
						// the selected Square to check if it checks the
						// opponent
						// King. If it does, check the opponent King
						ArrayList<Square> nextMovesPossible = move.piece
								.generateMoves(board);
						for (Square movePossible : nextMovesPossible)
							if (movePossible
									.containsOpponentKing(move.piece))
							{
								King kingToCheck = (King) movePossible.piece;
								kingToCheck.check(move);
								kingChecked = kingToCheck;
							}

						// Uncheck each King if it is no longer in threat
						// (handled
						// in the uncheck () method in each King)
						int kingsChecked = 0;
						for (int r = 0; r < board.length && kingsChecked < 2; r++)
							for (int c = 0; c < board[0].length
									&& kingsChecked < 2; c++)
							{
								Square sq = board[r][c];
								if (sq.containsPiece())
								{
									Piece piece = sq.piece;
									if (piece instanceof King)
									{
										King king = (King) piece;
										king.uncheck(board);
										kingsChecked++;
									}
								}
							}

						int score = alphaBetaMax(alpha, beta,
								lookAheadDepth - 1);

						if (score <= alpha)
						{
							square.addPiece(move.removePiece(), true);
							if (removedPiece != null)
								move.addPiece(removedPiece, true);

							if (kingChecked != null)
								kingChecked.uncheck(board);

							ArrayList<Square> nextMoves = square.piece
									.generateMoves(board);
							for (Square movePossible : nextMoves)
								if (movePossible
										.containsOpponentKing(square.piece))
								{
									King kingToCheck = (King) movePossible.piece;
									kingToCheck.check(move);
								}

							this.miniBestPiece = square.piece;
							this.miniBestSourceSquare = square;
							this.miniBestMove = move;
							return alpha;
						}
						if (score < beta)
						{
							beta = score;
							bestPiece = move.piece;
							bestSourceSquare = square;
							bestMove = move;
						}

						// lookAheadMoves.remove(lookAheadMoves.size() - 1);

						/*
						 * if (score < minScore) { minScore = score; bestPiece =
						 * move.piece; bestSourceSquare = square; bestMove =
						 * move; }
						 */

						square.addPiece(move.removePiece(), true);
						if (removedPiece != null)
							move.addPiece(removedPiece, true);

						if (kingChecked != null)
							kingChecked.uncheck(board);

						ArrayList<Square> nextMoves = square.piece
								.generateMoves(board);
						for (Square movePossible : nextMoves)
							if (movePossible.containsOpponentKing(square.piece))
							{
								King kingToCheck = (King) movePossible.piece;
								kingToCheck.check(move);
							}
					}
				}
			}

		this.miniBestPiece = bestPiece;
		this.miniBestSourceSquare = bestSourceSquare;
		this.miniBestMove = bestMove;

		return beta;
	}

	/**
	 * Undoes the last move, as well as the second last move if the user plays
	 * against the computer. Sets who plays next accordingly. Also accounts for
	 * Kings if they move out of a check after undo move is made. Also, checks
	 * if the game has ended by draw or check mate
	 */
	public void undo()
	{
		// If the game can be undone, do the following
		if (canUndo())
		{
			// Undo the last move made, as well the second last move if playing
			// against the computer
			Move lastMove = moves.removeLast();
			lastMove.undo(capturedPieces);
			if (vsComputerScreen)
			{
				Move secondLastMove = moves.removeLast();
				secondLastMove.undo(capturedPieces);
			}

			// Another undo move cannot be made if in a two player game. Also,
			// the next turn must be taken by the alternate player if in a two
			// player game, by the same player in a single player game
			if (twoPlayerScreen)
			{
				moveUndone = true;
				turn *= -1;
			}

			// Uncheck each King if it is no longer in threat (handled
			// in the uncheck () method in each King)
			int kingsChecked = 0;
			for (int row = 0; row < board.length && kingsChecked < 2; row++)
				for (int col = 0; col < board[0].length
						&& kingsChecked < 2; col++)
				{
					Square square = board[row][col];
					if (square.containsPiece() && square.piece instanceof King)
					{
						King king = (King) square.piece;
						king.uncheck(board);
						kingsChecked++;
					}
				}

			// Check if the game has ended after undoing
			checkEndOfGame();

			repaint();
		}
	}

	/**
	 * Checks if moves can be undone in the current game according to whether
	 * the user plays against the computer or another player
	 * @return true if the game can be undone, false otherwise
	 */
	public boolean canUndo()
	{
		// If the user plays against the computer and moves have been made
		// previously, both the user and computer must have completed a move
		// each so that both may be undone
		if (vsComputerScreen && !moves.isEmpty() && !moveUndone)
		{
			if (playerColor == 1)
				return (moves.size() % 2 == 0);
			else
				return (moves.size() > 1 && moves.size() % 2 == 1);
		}

		// If the user plays against another player, a move can be undone if it
		// is the last move in the List of moves
		else if (twoPlayerScreen)
			return (!moves.isEmpty() && !moveUndone);

		// Moves cannot be undone if none of the above conditions are met
		else
			return false;
	}

	/**
	 * Checks whether a game can be drawn if it is not over in special
	 * conditions, such as if the current game satisfies the fifty move rule
	 * @return true if the game can be drawn, false otherwise
	 */
	public boolean canDrawGame()
	{
		// If the game is over, it cannot be drawn
		if (gameOver)
			return false;

		// Account for the fifty move rule
		if (moves.size() >= 100)
		{
			int checkTill = moves.size() - 100;
			boolean canDraw = true;
			for (int move = moves.size() - 1; move >= checkTill && canDraw; move--)
			{
				Move lastMove = moves.get(move);
				if (lastMove.pawnMoved() || lastMove.pieceCaptured())
					canDraw = false;
			}

			if (canDraw)
				return true;
		}

		return false;
	}

	/**
	 * Draws the current game if it can be drawn
	 */
	public void drawGame()
	{
		if (canDrawGame())
		{
			gameOver = true;
			drawCalled = true;
			endGameType = "Draw Declared";
		}

		repaint();
	}

	/**
	 * Checks whether the current game has been won in a check mate according to
	 * the rules of the game of Chess. If not a check mate, also checks if the
	 * game is drawn due to stale mate or due to insufficient material on either
	 * side to check mate the other side. Sets the type of end game scenario
	 * accordingly.
	 */
	public void checkEndOfGame()
	{
		// Keep track of which side's turn it is using its color
		int turnColor = 0;
		if (turn == -1)
			turnColor = 1;

		// Keep track of the number of pieces, on the side whose turn it is,
		// that can be legally moved
		boolean piecesCanBeMoved = false;

		// Go through each Piece on the board and generate moves from it
		// (including handling checks) to see if it can be moved. If a single
		// piece on the side whose turn it is currently can move, pieces can be
		// moved
		for (int row = 0; row < board.length && !piecesCanBeMoved; row++)
			for (int col = 0; col < board[row].length && !piecesCanBeMoved; col++)
			{
				Square check = board[row][col];
				if (check.containsPiece() && check.piece.color == turnColor)
				{
					ArrayList<Square> movesPossible = check.piece
							.generateMoves(board);
					handleChecks(movesPossible, check, check.piece);
					if (!movesPossible.isEmpty())
						piecesCanBeMoved = true;
				}
			}

		// If no pieces of pieces in the current turn exist, the game is over.
		// If the king is in check, a check mate has occurred. Else, a draw by
		// stale mate has occurred
		if (!piecesCanBeMoved)
		{
			gameOver = true;
			boolean kingFound = false;
			for (int row = 0; row < board.length && !kingFound; row++)
				for (int col = 0; col < board[row].length && !kingFound; col++)
				{
					Square check = board[row][col];
					if (check.containsPiece() && check.piece instanceof King
							&& check.piece.color == turnColor)
					{
						King ownKing = (King) check.piece;

						if (ownKing.inCheck())
							endGameType = endGameTypes[0];
						else
							endGameType = endGameTypes[1];

						kingFound = true;
					}
				}

			return;
		}

		// Account for draw due to insufficient material
		boolean insufficientMaterial = true;

		// Keep track of all the white and black pieces currently in the game
		ArrayList<Piece> whitePieces = new ArrayList<Piece>();
		ArrayList<Piece> blackPieces = new ArrayList<Piece>();

		// For each Square on the board that contains a Piece, do the following
		for (int row = 0; row < board.length && insufficientMaterial; row++)
			for (int col = 0; col < board[row].length && insufficientMaterial; col++)
			{
				Square check = board[row][col];

				// If the Piece is a pawn, there exists sufficient material to
				// checkmate
				if (check.containsPiece() && check.piece instanceof Pawn)
					insufficientMaterial = false;

				// If the Piece is not a pawn, add it to the list of white/black
				// pieces appropriately
				else if (check.containsPiece())
				{
					if (check.piece.color == 0)
						whitePieces.add(check.piece);
					else
						blackPieces.add(check.piece);
				}
			}

		// If there are no pawns left (insufficient material) and only two Kings
		// remain on the board, there exists insufficient material for check
		// mate
		if (insufficientMaterial && whitePieces.size() == 1
				&& whitePieces.get(0) instanceof King
				&& blackPieces.size() == 1
				&& blackPieces.get(0) instanceof King)
			insufficientMaterial = true;

		// If there are no pawns left and only one white King and two black
		// pieces remain on the board, do the following
		else if (insufficientMaterial && whitePieces.size() == 1
				&& whitePieces.get(0) instanceof King
				&& blackPieces.size() == 2)
		{
			// Keep track of the remaining black pieces
			Piece lastBlackPiece = blackPieces.get(0);
			Piece secondLastBlackPiece = blackPieces.get(1);

			// If one of the black pieces is a King while the other is a Knight
			// or a Bishop, there is insufficient material. Else, sufficient
			// material for check mate exists
			if (lastBlackPiece instanceof King
					&& (secondLastBlackPiece instanceof Knight || secondLastBlackPiece instanceof Bishop))
				insufficientMaterial = true;
			else if (secondLastBlackPiece instanceof King
					&& (lastBlackPiece instanceof Knight || lastBlackPiece instanceof Bishop))
				insufficientMaterial = true;
			else
				insufficientMaterial = false;
		}

		// If there are no pawns left and only one black King and two white
		// pieces remain on the board, do the following
		else if (insufficientMaterial && blackPieces.size() == 1
				&& blackPieces.get(0) instanceof King
				&& whitePieces.size() == 2)
		{
			// Keep track of the remaining white pieces
			Piece lastWhitePiece = whitePieces.get(0);
			Piece secondLastWhitePiece = whitePieces.get(1);

			// If one of the white pieces is a King while the other is a Knight
			// or a Bishop, there is insufficient material. Else, sufficient
			// material for check mate exists
			if (lastWhitePiece instanceof King
					&& (secondLastWhitePiece instanceof Knight || secondLastWhitePiece instanceof Bishop))
				insufficientMaterial = true;
			else if (secondLastWhitePiece instanceof King
					&& (lastWhitePiece instanceof Knight || lastWhitePiece instanceof Bishop))
				insufficientMaterial = true;
			else
				insufficientMaterial = false;
		}

		// If only two pieces of each color remain on the board, do the
		// following
		else if (insufficientMaterial && blackPieces.size() == 2
				&& whitePieces.size() == 2)
		{
			// Keep track of the remaining pieces
			Piece lastWhitePiece = whitePieces.get(0);
			Piece secondLastWhitePiece = whitePieces.get(1);
			Piece lastBlackPiece = blackPieces.get(0);
			Piece secondLastBlackPiece = blackPieces.get(1);

			// If the one of the white Pieces remaining is a Bishop and the
			// other is a King, as well as one of the black Pieces is a Bishop
			// and the other is a King, insufficient material exists. Else,
			// suficient material for checkmate exists
			if (lastWhitePiece instanceof Bishop)
			{
				if (secondLastWhitePiece instanceof King)
				{
					if (lastBlackPiece instanceof Bishop)
					{
						if (secondLastBlackPiece instanceof King)
							insufficientMaterial = true;
						else
							insufficientMaterial = false;
					}
					else if (secondLastBlackPiece instanceof Bishop)
					{
						if (lastBlackPiece instanceof King)
							insufficientMaterial = true;
						else
							insufficientMaterial = false;
					}
					else
						insufficientMaterial = false;
				}
				else
					insufficientMaterial = false;
			}

			else if (secondLastWhitePiece instanceof Bishop)
			{
				if (lastWhitePiece instanceof King)
				{
					if (lastBlackPiece instanceof Bishop)
					{
						if (secondLastBlackPiece instanceof King)
							insufficientMaterial = true;
						else
							insufficientMaterial = false;
					}
					else if (secondLastBlackPiece instanceof Bishop)
					{
						if (lastBlackPiece instanceof King)
							insufficientMaterial = true;
						else
							insufficientMaterial = false;
					}
					else
						insufficientMaterial = false;
				}
				else
					insufficientMaterial = false;
			}

			else
				insufficientMaterial = false;
		}

		// In all other scenarios, there exists sufficient material for a
		// possible check mate
		else
			insufficientMaterial = false;

		// If there exists insufficient material, the game is over by draw
		if (insufficientMaterial)
		{
			gameOver = true;
			endGameType = "Draw by impossibility of checkmate";
		}

		// If neither check mate, nor stale mate, nor draw by insufficient
		// material occur, the game is not over yet
		else
		{
			gameOver = false;
			endGameType = null;
		}
	}

	/**
	 * Handles moves of the given selected piece when the King is under check,
	 * or when a specific move of the given selected piece may put its king in a
	 * check
	 * @param movesGenerated the original ArrayList of Squares to which the
	 *            selected Piece can move
	 * @param pieceSelected the selected Piece whose moves have been selected
	 * @param source the source Square from which the piece is selected
	 *            Postcondition: All the Squares to which the selected piece
	 *            cannot move legally are removed from the given original
	 *            ArrayList of moves
	 */
	public void handleChecks(ArrayList<Square> movesGenerated,
			Square source, Piece pieceSelected)
	{
		// Keep track of all the Squares from the original moves
		// to which this Piece cannot be added in the actual
		// game
		ArrayList<Square> movesNotPossible = new
				ArrayList<Square>();

		// For each original Square this Piece can move to, do
		// the following
		for (Square move : movesGenerated)
		{
			// Add this Piece to that Square for checking
			// purposes and remove any existing Piece
			Piece removedPiece = null;
			removedPiece = move.addPiece(pieceSelected, true);
			source.removePiece();

			// Keep track of whether the selected piece's King
			// comes under check when this Piece is moved to the
			// original move Square being checked
			boolean putsKingInCheck = false;

			// For each Square on the board that contains a
			// non-King piece which is an opponent of the
			// selected Piece, do the following
			for (int checkRow = 0; checkRow < board.length
					&& !putsKingInCheck; checkRow++)
				for (int checkCol = 0; checkCol < board[checkRow].length
						&& !putsKingInCheck; checkCol++)
				{
					Square checkSq = board[checkRow][checkCol];
					if (checkSq.containsPiece()
							&& !(checkSq.piece instanceof King)
							&& !checkSq.piece
									.isSameColor(pieceSelected))
					{
						// Generate all the moves from this
						// opponent Piece
						ArrayList<Square> nextMovesPossible =
								checkSq.piece
										.generateMoves(board);

						// If the opponent Piece can attack the
						// selected piece's King, the selected
						// piece cannot be moved to the original
						// Square being checked
						for (Square next : nextMovesPossible)
						{
							if (next.containsOwnKing(pieceSelected))
							{
								movesNotPossible.add(move);
								putsKingInCheck = true;
							}
						}
					}
				}

			// Restore the original positions of all Pieces
			move.removePiece();
			source.addPiece(pieceSelected, true);
			if (removedPiece != null)
				move.addPiece(removedPiece, true);
		}

		// Remove all the Squares to which this Piece cannot
		// move from the original moves
		for (Square next : movesNotPossible)
			movesGenerated.remove(next);

	}

	// Dummy method
	@Override
	public void mouseClicked(MouseEvent event)
	{

	}

	/**
	 * Handles pressing a mouse on any screen of the game. If in a single or two
	 * player game, handles selecting pieces, generating moves and dropping
	 * Pieces to selected squares. Further handles special moves, such as
	 * promotion, en passant and castling if they are made by the selected
	 * piece, and checks if Kings are put in or removed out of a check when the
	 * selected Piece is moved in the current game
	 * @param event event information for mouse pressed
	 */
	@Override
	public void mousePressed(MouseEvent event)
	{
		// Get the Point where the mouse is clicked
		Point selectedPoint = event.getPoint();

		// If the mouse is clicked on one of the buttons on the main screen,
		// proceed the user to the appropriate screen
		if (mainScreen)
		{
			// If the user chooses to play against the computer, proceed to the
			// choice of difficulty screen
			if (vsComputerButton.contains(selectedPoint))
			{
				mainScreen = false;
				difficultyScreen = false;
				twoPlayerScreen = false;
				difficultyScreen = true;
				vsComputerScreen = false;
			}

			// If the user chooses to play against another player, start a new
			// two player game
			else if (twoPlayerButton.contains(selectedPoint))
			{
				mainScreen = false;
				newGame();
				twoPlayerScreen = true;
				vsComputerScreen = false;
			}
		}

		// If the user selects one of the difficulty choices on the difficulty
		// screen, set the appropriate difficulty of the computer play and
		// proceed to the choice of side screen
		else if (difficultyScreen)
		{
			if (easyButton.contains(selectedPoint))
			{
				aiDifficulty = 2;
				difficultyScreen = false;
				colorChoiceScreen = true;
			}
			else if (mediumButton.contains(selectedPoint))
			{
				aiDifficulty = 3;
				difficultyScreen = false;
				colorChoiceScreen = true;
			}
			else if (hardButton.contains(selectedPoint))
			{
				aiDifficulty = 4;
				difficultyScreen = false;
				colorChoiceScreen = true;
			}
			else if (veryHardButton.contains(selectedPoint))
			{
				aiDifficulty = 5;
				difficultyScreen = false;
				colorChoiceScreen = true;
			}
		}

		// Keep track of what color the user plays as versus the computer and
		// start a new single player game. Make a white computer move first if
		// the player chooses black
		else if (colorChoiceScreen)
		{
			if (whiteButton.contains(selectedPoint))
			{
				playerColor = 1;
				colorChoiceScreen = false;
				vsComputerScreen = true;
				newGame();
			}
			else if (blackButton.contains(selectedPoint))
			{
				playerColor = -1;
				colorChoiceScreen = false;
				vsComputerScreen = true;
				newGame();
				playComputer();
			}
		}

		// If the mouse is clicked during a two player game, do the following
		else if (twoPlayerScreen)
		{
			// If the selected point is contained in the chess board, do the
			// following
			if (containedInBoard(selectedPoint))
			{
				// Keep track of the Square which has been selected
				Square selectedSquare = getSelectedSquare(selectedPoint);

				// If no piece has been selected yet, do the following
				if (selectedPiece == null)
				{
					// If the selected Square contains a Piece, generate moves
					// from it and set it to the selected Piece. Also update the
					// source Square
					if (selectedSquare.containsPiece())
					{
						// If the appropriate color of piece is chosen according
						// to whose turn it is, do the following
						if ((turn == 1 && selectedSquare.piece.color == 0)
								|| (turn == -1 && selectedSquare.piece.color == 1))
						{
							// Keep track of the moves generated from the
							// selected
							// Piece and set the source square and selected
							// piece
							// accordingly
							movesGenerated = selectedSquare.piece
									.generateMoves(board);
							selectedPiece = selectedSquare.piece;
							sourceSquare = selectedSquare;

							// Handle situations for moving the selected piece
							// when its King may be under check, or when moving
							// the piece may produce a check
							handleChecks(movesGenerated, sourceSquare,
									selectedPiece);

							// Highlight all the Squares to which the selected
							// Piece can move. Also, select the square (set its
							// background image)
							for (Square toHighlight : movesGenerated)
								toHighlight.highlight();
						}
					}
				}

				// Else if a Piece has already been selected, do the following
				else
				{
					if (movesGenerated.indexOf(selectedSquare) >= 0)
					{
						sourceSquare.removePiece();
						turn *= -1;

						// If a move has been made after a pawn has moved two
						// spaces for the first time, no en passant move can be
						// made on it by another Pawn
						if (!moves.isEmpty())
						{
							Piece lastPieceMoved = moves.getLast()
									.getPieceMoved();
							for (int row = 0; row < board.length; row++)
								for (int col = 0; col < board[row].length; col++)
									if (board[row][col].containsPiece())
									{
										Piece piece = board[row][col].piece;
										if (piece instanceof Pawn)
										{
											Pawn pawn = (Pawn) piece;
											if (!pawn.equals(lastPieceMoved))
												pawn.enPassantPossible = false;
										}
									}
						}

						// Check for an en passant move and capture the en
						// passant Pawn
						if (selectedPiece instanceof Pawn &&
								!selectedSquare.containsPiece())
						{
							Pawn selectedPawn = (Pawn) selectedPiece;
							if (!selectedPawn.onOppositeSide)
							{
								Piece removed = board[selectedSquare.row + 1][selectedSquare.column]
										.removePiece();
								if (removed != null)
								{
									removed.capture(capturedPieces);
									capturedPieces.add(removed);
								}
								board[selectedSquare.row + 1][selectedSquare.column]
										.unhighlight();
							}
							else
							{
								Piece removed = board[selectedSquare.row - 1][selectedSquare.column]
										.removePiece();
								if (removed != null)
								{
									removed.capture(capturedPieces);
									capturedPieces.add(removed);
								}
								board[selectedSquare.row - 1][selectedSquare.column]
										.unhighlight();
							}
						}

						// Add the Piece to the selected Square and if any Piece
						// is captured, add it to the ArrayList of captured
						// Pieces
						Piece capturedPiece = selectedSquare
								.addPiece(selectedPiece, false);
						if (capturedPiece != null)
						{
							capturedPiece.capture(capturedPieces);
							capturedPieces.add(capturedPiece);
						}

						if (selectedPiece instanceof King)
						{
							// If the Piece is a King and moves two spaces, it
							// must be castling. Move the Rook the King is
							// castling with accordingly
							King king = (King) selectedPiece;
							king.hasMoved = true;
							if (selectedSquare.column - sourceSquare.column != Math
									.abs(1))
							{
								Rook rook;

								// Handle castling King side
								if (selectedSquare.column == 6
										&& board[selectedSquare.row][selectedSquare.column + 1].piece instanceof Rook)
								{
									rook = (Rook) board[selectedSquare.row][selectedSquare.column + 1].piece;
									board[rook.row][rook.column].removePiece();
									board[selectedSquare.row][selectedSquare.column - 1]
											.addPiece(rook, false);
									rook.castle(selectedSquare.row,
											selectedSquare.column + 1);
								}

								// Handle castling Queen Side
								else if (selectedSquare.column == 2
										&& board[selectedSquare.row][selectedSquare.column - 2].piece instanceof Rook)
								{
									rook = (Rook) board[selectedSquare.row][selectedSquare.column - 2].piece;
									board[rook.row][rook.column].removePiece();
									board[selectedSquare.row][selectedSquare.column + 1]
											.addPiece(rook, false);
									rook.castle(selectedSquare.row,
											selectedSquare.column - 2);

								}

							}
						}

						// If a Pawn is being moved to the back row of the
						// opposite side, display a message giving the user
						// possible choice of piece to promote the pawn to.
						// Remove the pawn being promoted and add the promoted
						// Piece selected by the user in its place
						if (selectedPiece instanceof Pawn
								&& (selectedPiece.row == 0
								|| selectedPiece.row == 7))
						{
							String[] promotionChoices = { "Queen", "Knight",
									"Bishop", "Rook" };
							int promotionChoice = JOptionPane
									.showOptionDialog(
											chessFrame,
											"Please choose the piece you wish to promote your pawn to",
											"Promotion Piece Choices",
											JOptionPane.YES_NO_CANCEL_OPTION,
											JOptionPane.QUESTION_MESSAGE,
											null,
											promotionChoices,
											promotionChoices[0]);
							selectedSquare.removePiece();
							Piece promotedPiece;
							if (promotionChoice == 0)
								promotedPiece = new Queen(selectedSquare.row,
										selectedSquare.column,
										selectedPiece.color);
							else if (promotionChoice == 1)
								promotedPiece = new Knight(selectedSquare.row,
										selectedSquare.column,
										selectedPiece.color);
							else if (promotionChoice == 2)
								promotedPiece = new Bishop(selectedSquare.row,
										selectedSquare.column,
										selectedPiece.color);
							else
								promotedPiece = new Rook(selectedSquare.row,
										selectedSquare.column,
										selectedPiece.color);
							selectedSquare.addPiece(promotedPiece, false);
						}

						// Generate further possible moves after moving the
						// Piece to
						// the selected Square to check if it checks the
						// opponent
						// King. If it does, check the opponent King
						ArrayList<Square> nextMovesPossible = selectedSquare.piece
								.generateMoves(board);
						for (Square movePossible : nextMovesPossible)
							if (movePossible
									.containsOpponentKing(selectedPiece))
							{
								King kingToCheck = (King) movePossible.piece;
								kingToCheck.check(selectedSquare);
							}

						// Uncheck each King if it is no longer in threat
						// (handled
						// in the uncheck () method in each King)
						int kingsChecked = 0;
						for (int row = 0; row < board.length
								&& kingsChecked < 2; row++)
							for (int col = 0; col < board[0].length
									&& kingsChecked < 2; col++)
							{
								Square square = board[row][col];
								if (square.containsPiece())
								{
									Piece piece = square.piece;
									if (piece instanceof King)
									{
										King king = (King) piece;
										king.uncheck(board);
										kingsChecked++;
									}
								}
							}

						// Add this Move to the list of Moves and set the undo
						// option of the frame to true since at least this move
						// can be undone
						moves.addLast(new Move(sourceSquare, selectedSquare,
								selectedPiece, capturedPiece));
						moveUndone = false;
						chessFrame.setUndoOption(true);
					}

					// Unhighlight all the previously generated Squares
					for (Square moveGenerated : movesGenerated)
						moveGenerated.unhighlight();

					// Set the selected piece, square and the moves generated to
					// null
					movesGenerated = null;
					selectedPiece = null;
					sourceSquare = null;
				}

			}

			// If the user clicks on one of the buttons in the side panel, such
			// as Undo, New Game, Statistics or Quit, display the appropriate
			// message or perform the required command
			if (undoButton.contains(selectedPoint))
				this.undo();

			else if (newGameButton.contains(selectedPoint))
				newGame();

			else if (statisticsButton.contains(selectedPoint))
			{
				Statistics myStats = Statistics.readFromFile("stats.dat");
				JOptionPane.showMessageDialog(this,
						"Number of wins " + myStats.numWins
								+ "\n Number of Games played "
								+ myStats.numGames
								+ "\n Win Percentage " + myStats.winPercent(),
						"Statistics", JOptionPane.INFORMATION_MESSAGE);
			}

			else if (mainMenuButton.contains(selectedPoint))
			{
				twoPlayerScreen = false;
				mainScreen = true;
			}

		}

		// If the user plays versus the computer, do the following
		else if (vsComputerScreen)
		{
			// If the selected point is contained in the chess board, do the
			// following
			if (containedInBoard(selectedPoint))
			{
				// Keep track of the Square which has been selected
				Square selectedSquare = getSelectedSquare(selectedPoint);

				// If no piece has been selected yet, do the following
				if (selectedPiece == null)
				{
					// If the selected Square contains a Piece, generate moves
					// from
					// it and set it to the selected Piece. Also update the
					// source
					// Square
					if (selectedSquare.containsPiece())
					{
						// If it is the user's turn and he/she selects the piece
						// of the correct color, do the following
						if (turn == playerColor
								&& ((playerColor == 1 && selectedSquare.piece.color == 0) || (playerColor == -1 && selectedSquare.piece.color == 1)))
						{
							// Keep track of the moves generated from the
							// selected
							// Piece and set the source square and selected
							// piece
							// accordingly
							movesGenerated = selectedSquare.piece
									.generateMoves(board);
							selectedPiece = selectedSquare.piece;
							sourceSquare = selectedSquare;

							// Handle situations for moving the selected piece
							// when its King may be under check, or moving the
							// piece may produce a check
							handleChecks(movesGenerated, sourceSquare,
									selectedPiece);

							// Highlight all the Squares to which the selected
							// Piece can move. Also, select the square (set its
							// background image)
							if (!movesGenerated.isEmpty())
								selectedSquare.select();
							for (Square toHighlight : movesGenerated)
								toHighlight.highlight();
						}
					}
				}

				// Else if a Piece has already been selected, do the following
				else if (turn == playerColor)
				{
					// If the selected Piece can be moved to the selected
					// Square, remove it from the source Square and add it to
					// the selected Square. Also, keep track of the position of
					// the selected Piece
					Point pos = new Point(selectedPiece.getPosition());
					if (movesGenerated.indexOf(selectedSquare) >= 0)
					{
						sourceSquare.removePiece();
						turn *= -1;

						// Add the Piece to the selected Square and if any Piece
						// is
						// captured, add it to the ArrayList of captured Pieces
						Piece capturedPiece = selectedSquare
								.addPiece(selectedPiece, false);
						if (capturedPiece != null)
						{
							capturedPiece.capture(capturedPieces);
							capturedPieces.add(capturedPiece);
						}

						if (selectedPiece instanceof King)
						{
							// If the Piece is a King and moves two spaces, it
							// must be castling. Move the Rook the King is
							// castling with accordingly
							King king = (King) selectedPiece;
							king.hasMoved = true;

							if (selectedSquare.column - sourceSquare.column != Math
									.abs(1))
							{
								Rook rook;

								// Handle castling King side
								if (selectedSquare.column == 6
										&& board[selectedSquare.row][selectedSquare.column + 1].piece instanceof Rook)
								{
									rook = (Rook) board[selectedSquare.row][selectedSquare.column + 1].piece;
									board[rook.row][rook.column].removePiece();
									board[selectedSquare.row][selectedSquare.column - 1]
											.addPiece(rook, false);
									rook.castle(selectedSquare.row,
											selectedSquare.column + 1);

								}

								// Handle castling Queen Side
								else if (selectedSquare.column == 2
										&& board[selectedSquare.row][selectedSquare.column - 2].piece instanceof Rook)
								{
									rook = (Rook) board[selectedSquare.row][selectedSquare.column - 2].piece;
									board[rook.row][rook.column].removePiece();
									board[selectedSquare.row][selectedSquare.column + 1]
											.addPiece(rook, false);
									rook.castle(selectedSquare.row,
											selectedSquare.column - 2);

								}

							}
						}

						// If a Pawn is being moved to the back row of the
						// opposite side, display a message giving the user
						// possible choice of piece to promote the pawn to.
						// Remove the pawn being promoted and add the promoted
						// Piece selected by the user in its place
						if (selectedPiece instanceof Pawn
								&& (selectedPiece.row == 0
								|| selectedPiece.row == 7))
						{

							String[] promotionChoices = { "Queen", "Knight",
									"Bishop", "Rook" };
							int promotionChoice = JOptionPane
									.showOptionDialog(
											chessFrame,
											"Please choose the piece you wish to promote your pawn to",
											"Promotion Piece Choices",
											JOptionPane.YES_NO_CANCEL_OPTION,
											JOptionPane.QUESTION_MESSAGE,
											null,
											promotionChoices,
											promotionChoices[0]);
							selectedSquare.removePiece();
							Piece promotedPiece;
							if (promotionChoice == 0)
								promotedPiece = new Queen(selectedSquare.row,
										selectedSquare.column,
										selectedPiece.color);
							else if (promotionChoice == 1)
								promotedPiece = new Knight(selectedSquare.row,
										selectedSquare.column,
										selectedPiece.color);
							else if (promotionChoice == 2)
								promotedPiece = new Bishop(selectedSquare.row,
										selectedSquare.column,
										selectedPiece.color);
							else
								promotedPiece = new Rook(selectedSquare.row,
										selectedSquare.column,
										selectedPiece.color);
							selectedSquare.addPiece(promotedPiece, false);
						}

						// Generate further possible moves after moving the
						// Piece to
						// the selected Square to check if it checks the
						// opponent
						// King. If it does, check the opponent King
						ArrayList<Square> nextMovesPossible = selectedSquare.piece
								.generateMoves(board);
						for (Square movePossible : nextMovesPossible)
							if (movePossible
									.containsOpponentKing(selectedPiece))
							{
								King kingToCheck = (King) movePossible.piece;
								kingToCheck.check(selectedSquare);
							}

						// Uncheck each King if it is no longer in threat
						// (handled
						// in the uncheck () method in each King)
						int kingsChecked = 0;
						for (int row = 0; row < board.length
								&& kingsChecked < 2; row++)
							for (int col = 0; col < board[0].length
									&& kingsChecked < 2; col++)
							{
								Square square = board[row][col];
								if (square.containsPiece())
								{
									Piece piece = square.piece;
									if (piece instanceof King)
									{
										King king = (King) piece;
										king.uncheck(board);
										kingsChecked++;
									}
								}
							}

						// Add this Move to the list of Moves and set the undo
						// option of the frame to true since at least this move
						// can
						// be undone
						playerMoveMade = true;
						moves.addLast(new Move(sourceSquare, selectedSquare,
								selectedPiece, capturedPiece));
						moveUndone = false;
						chessFrame.setUndoOption(true);

						// Unhighlight all the previously generated Squares
						for (Square moveGenerated : movesGenerated)
							moveGenerated.unhighlight();

						// Set the position of the Piece to the Square being
						// moved to so that its image can be drawn before the
						// computer's move can be made
						Point finalPos = new Point(selectedPiece.getPosition());
						moveAPiece(selectedPiece, pos, finalPos);

						// Make the computer's move
						playComputer();
					}

					// Unhighlight all the previously generated Squares
					for (Square moveGenerated : movesGenerated)
						moveGenerated.unhighlight();

					movesGenerated = null;
					selectedPiece = null;
					sourceSquare = null;
				}

				selectedSquare.deselect();

			}

			// If the user clicks on one of the buttons in the side panel, such
			// as Undo, New Game, Statistics or Quit, display the appropriate
			// message or perform the required command
			if (undoButton.contains(selectedPoint))
				this.undo();

			else if (newGameButton.contains(selectedPoint))
			{
				newGame();
				if (playerColor == -1)
					playComputer();
			}

			else if (statisticsButton.contains(selectedPoint))
			{
				Statistics myStats = Statistics.readFromFile("stats.dat");
				JOptionPane.showMessageDialog(this,
						"Number of wins " + myStats.numWins
								+ "\n Number of Games played "
								+ myStats.numGames
								+ "\n Win Percentage " + myStats.winPercent(),
						"Statistics", JOptionPane.INFORMATION_MESSAGE);
			}

			else if (mainMenuButton.contains(selectedPoint))
			{
				vsComputerScreen = false;
				mainScreen = true;
			}

		}

		repaint();
	}

	/**
	 * Handles the mouse being moved and changing the cursor on each screen
	 * @param event event information for mouse moved
	 */
	@Override
	public void mouseMoved(MouseEvent event)
	{
		// Get the current point of the mouse
		Point currentPoint = event.getPoint();

		// If the mouse is over one of the buttons on the main screen, change
		// the cursor to a hand for clicking
		if (mainScreen)
		{
			if (vsComputerButton.contains(currentPoint)
					|| twoPlayerButton.contains(currentPoint))
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			else
				setCursor(Cursor.getDefaultCursor());
		}

		// If the mouse is over one of the buttons on the difficulty screen,
		// change the cursor to a hand
		else if (difficultyScreen)
		{
			if (easyButton.contains(currentPoint)
					|| mediumButton.contains(currentPoint)
					|| hardButton.contains(currentPoint)
					|| veryHardButton.contains(currentPoint))
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			else
				setCursor(Cursor.getDefaultCursor());
		}

		// If the mouse is over one of the buttons on the color choice screen,
		// change the cursor to a hand
		else if (colorChoiceScreen)
		{
			if (whiteButton.contains(currentPoint)
					|| blackButton.contains(currentPoint))
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			else
				setCursor(Cursor.getDefaultCursor());
		}

		// If a game is in progress, do the following
		else if (vsComputerScreen || twoPlayerScreen)
		{
			// If the cursor is over a selected piece on the board or over a
			// square to which a selected piece can move to according to whose
			// turn it currently is, change it to a hand
			if (containedInBoard(currentPoint))
			{
				Square mouseOver = getSelectedSquare(currentPoint);
				if (selectedPiece == null && mouseOver.containsPiece()
						&& !mouseOver.piece.generateMoves(board).isEmpty())
				{
					if (turn == 1 && mouseOver.piece.color == 0)
						setCursor(Cursor
								.getPredefinedCursor(Cursor.HAND_CURSOR));
					else if (turn == -1 && mouseOver.piece.color == 1)
						setCursor(Cursor
								.getPredefinedCursor(Cursor.HAND_CURSOR));
					else
						setCursor(Cursor.getDefaultCursor());
				}
				else if (selectedPiece != null
						&& movesGenerated.indexOf(mouseOver) >= 0)
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				else
					setCursor(Cursor.getDefaultCursor());
			}

			// If the mouse is over the new game, statistics or main menu
			// buttons, change it to a hand
			else if (newGameButton.contains(currentPoint)
					|| statisticsButton.contains(currentPoint)
					|| mainMenuButton.contains(currentPoint))
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

			// Change the cursor to hand if it is over the undo button and a
			// move can be undone
			else if (undoButton.contains(currentPoint) && canUndo())
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			else
				setCursor(Cursor.getDefaultCursor());
		}
	}

	// Dummy methods
	@Override
	public void mouseDragged(MouseEvent event)
	{

	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{

	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{

	}

	@Override
	public void mouseReleased(MouseEvent event)
	{

	}
}
