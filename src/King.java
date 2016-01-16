import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * The King piece in a Chess game, can move and generate positions to move to
 * 
 * @author Sean and Siddharth
 * @version 20/01/2015
 */
public class King extends Piece
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Keep track of whether this King is in a check and which Squares directly
	// check this King according to the rules of Chess
	private boolean inCheck;
	private ArrayList<Square> threatSquares;
	// Sets the value of the King
	private static int value = 32676;
	private static int[][] kingScoreTable = {
			{ -100, -100, -100, -100, -100, -100, -100, -100 },
			{ -100, -100, -100, -100, -100, -100, -100, -100 },
			{ -100, -100, -100, -100, -100, -100, -100, -100 },
			{ -50, -50, -50, -50, -50, -50, -50, -50 },
			{ -10, 0, 10, 10, 10, 10, 0, -10 },
			{ -10, 10, 10, 10, 10, 10, 10, -10 },
			{ 30, 25, 0, 0, 0, 0, 20, 20 }, { 20, 10, 60, 10, 10, 0, 60, 20 } };
	private static final int[] CHECK_ROW = { 0, -1, -1, -1, 0, 1, 1, 1 };
	private static final int[] CHECK_COL = { -1, -1, 0, 1, 1, 1, 0, -1 };

	public King(int row, int column, int color)
	{
		super(row, column, color, color == 0 ? new ImageIcon("Temp\\King0.png")
				.getImage() : new ImageIcon("Temp\\King1.png").getImage());

		// The King is not in check initially
		inCheck = false;
		threatSquares = new ArrayList<Square>();
	}

	/**
	 * Calculates the point value that the King adds to the game
	 * 
	 * @return The value of the Bishop based on its position
	 */
	public int getValue()
	{
		if (this.color == 0)
		{
			return value + kingScoreTable[this.row][this.column];
		}
		return value + kingScoreTable[7 - this.row][this.column];
	}

	/**
	 * Checks the King (when under threat from an opponent Piece)
	 * 
	 * @param threatSquare the Square which contains the Piece that directly
	 *            checks the King
	 */
	public void check(Square threatSquare)
	{
		inCheck = true;

		// Add the given threat Square to the ArrayList of Squares
		threatSquares.add(threatSquare);
	}

	/**
	 * Unchecks this King (when it is not in threat)
	 */
	public void uncheck(Square[][] board)
	{
		if (inCheck)
		{
			// Keep track of the Square containing this King on the given board
			Square kingSquare = board[this.row][this.column];

			// Keep track of squares which no longer threaten this King
			ArrayList<Square> noThreats = new ArrayList<Square>();

			for (Square threat : threatSquares)
			{
				// If the threat square contains a piece, it can possibly check
				// the king
				if (threat.containsPiece())
				{
					// If the piece in the threat square does not attack this
					// King, it does not check it
					ArrayList<Square> moves = threat.piece.generateMoves(board);
					if (!moves.contains(kingSquare))
						noThreats.add(threat);
				}

				// If there is no piece in the threat square, it no longer
				// threatens the King (for handling undo)
				else
					noThreats.add(threat);
			}

			// Remove all the squares that do not threaten this King anymore
			for (Square noThreat : noThreats)
				threatSquares.remove(noThreat);

			// If no squares threaten this King, it is no longer in check
			if (threatSquares.isEmpty())
				inCheck = false;
		}
	}

	/**
	 * Checks whether this King is in check
	 * 
	 * @return true if this King is in check, false otherwise
	 */
	public boolean inCheck()
	{
		return inCheck;
	}

	/**
	 * Gets all the Squares that check this King
	 * 
	 * @return an ArrayList of all Squares that threaten this King
	 */
	public ArrayList<Square> getThreatSquares()
	{
		return threatSquares;
	}

	/**
	 * Generates a list of legal moves for the King to make, including castling
	 * 
	 * @param board The board the game is being played on
	 * @return The list of legal moves the King can make
	 */
	public ArrayList<Square> generateMoves(Square[][] board)
	{
		// Keep track of the row and column of this King
		int row = super.row;
		int column = super.column;

		// Keep track of the ArrayList of Squares to which this King can legally
		// move in a game of Chess
		ArrayList<Square> validMoves = new ArrayList<Square>();
		int castleCheckCount = 0;
		// Castling
		// You cannot castle if the King has moved
		// hasMoved=false;
		if (!hasMoved && column == 4)
		{
			// Goes until the end is found
			for (int i = column + 1; i < board.length; i++)
			{
				// This is for potential castling through check cases
				castleCheckCount++;
				if (board[row][i].piece instanceof Rook
						&& (!board[row][i].piece.hasMoved)
						&& !this.inCheck
						&& (castleCheckCount >= 2 || !board[row][i]
								.threatenSquare(this.color, board)))
				{
					// Highlights the move
					board[row][i - 1].highlight();
					validMoves.add(board[row][i - 1]);
				}
				else if (board[row][i].containsPiece()
						|| board[row][i].threatenSquare(this.color, board))
				{
					i = 8;
				}
			}
			// Castling Queen side
			castleCheckCount = 0;
			for (int i = column - 1; i >= 0; i--)
			{
				castleCheckCount++;
				if (board[row][i].piece instanceof Rook
						&& (!board[row][i].piece.hasMoved) && !this.inCheck
						&& (!board[row][i].threatenSquare(this.color, board)))
				{
					board[row][i + 2].highlight();
					validMoves.add(board[row][i + 2]);
				}
				else if (board[row][i].containsPiece()
						|| board[row][i].threatenSquare(this.color, board)
						&& castleCheckCount < 2)
				{
					i = -1;
				}
			}
		}
		board[row][6].unhighlight();
		board[row][2].unhighlight();
		for (int check = 0; check < CHECK_ROW.length; check++)
		{
			// Keep track of the row and column of the Square being checked
			int checkRow = row + CHECK_ROW[check];
			int checkCol = column + CHECK_COL[check];

			// If the row and column being checked are within the board, do the
			// following
			if (checkRow >= 0 && checkRow < board.length && checkCol >= 0
					&& checkCol < board[row].length)
			{
				// Keep track of the Square being checked
				Square checkSquare = board[checkRow][checkCol];

				// If the Square being checked does not contain a Piece or it
				// contains a Piece of the opposite color, this King can
				// potentially be moved to it. So do the following to check if
				// the Square is under threat from an opponent Piece
				if (!checkSquare.containsPiece()
						|| !checkSquare.piece.isSameColor(this))
				{
					// Keep track of the removed piece from the Square (if any)
					// and add this King to the Square being checked for
					// checking purposes
					Piece removedPiece = null;
					removedPiece = checkSquare.addPiece(this, true);
					board[row][column].removePiece();

					// Keep track of whether the Square being checked is under
					// threat
					boolean underThreat = false;

					// Go through each Square around the current (potential
					// move) Square being checked in order to check for threats
					// from opponent Kings and Pawns (since Pawns attack
					// differently than they move and looking at all the moves
					// that the opponent King can make never terminates since
					// each King method calls the other repeatedly)
					for (int checkAround = 0; checkAround < CHECK_ROW.length
							&& !underThreat; checkAround++)
					{
						// Keep track of the current row and column of the
						// Square around the one being checked
						int checkR = checkRow + CHECK_ROW[checkAround];
						int checkC = checkCol + CHECK_COL[checkAround];

						// If the row and column around the Square being checked
						// are within the board, do the following
						if (checkR >= 0 && checkR < board.length && checkC >= 0
								&& checkC < board[row].length)
						{
							// Keep track of the current Square around the
							// Square being checked for
							Square checkFor = board[checkR][checkC];

							// If a Square around the Square being checked
							// contains an opponent King, it can be attacked
							if (checkFor.containsOpponentKing(this))
								underThreat = true;

							// If one of Squares diagonally around the Square
							// being checked contains an opponent Pawn, it can
							// be attacked
							else if (checkAround % 2 == 1
									&& checkFor.containsPiece()
									&& checkFor.piece instanceof Pawn
									&& !checkFor.piece.isSameColor(this))
								underThreat = true;
						}
					}

					// Go through each opponent Piece on the board to check if
					// it threatens the current potential Square being checked
					for (int boardRow = 0; boardRow < board.length
							&& !underThreat; boardRow++)
					{
						for (int boardCol = 0; boardCol < board[boardRow].length
								&& !underThreat; boardCol++)
						{
							// Keep track of the square on the board being
							// checked
							Square square = board[boardRow][boardCol];

							// If the square contains an opponent Piece that is
							// neither a King nor a Pawn (since these have been
							// handled before), do the following
							if (square.containsPiece()
									&& !square.piece.isSameColor(this)
									&& !(square.piece instanceof King)
									&& !(square.piece instanceof Pawn))
							{
								// Generate all the possible moves of the
								// opponent Piece and check whether any of these
								// include the current Square being checked, in
								// which case it is under threat
								ArrayList<Square> possibleMoves = square.piece
										.generateMoves(board);
								if (possibleMoves.indexOf(checkSquare) >= 0)
									underThreat = true;
							}
						}
					}

					// If the Square being checked is not under threat from any
					// opponent Piece, this King can be moved to it
					if (!underThreat)
						validMoves.add(checkSquare);

					// Add the King back to the original Square and add any
					// removed Piece to the Square checked
					checkSquare.removePiece();
					board[row][column].addPiece(this, true);
					if (removedPiece != null)
						checkSquare.addPiece(removedPiece, true);

				}
			}
		}

		// Return all the possible Squares to which this King can legally move
		return validMoves;
	}
}
