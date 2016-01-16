import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 * Keeps track of a Pawn class for a Chess game, and keeps track of whether is
 * on the opposite side and whether it can be captured in an en passant move.
 * Also keeps track o the value of the Pawn and a score table for its position
 * on the board. Includes methods to construct a Pawn given the row, column and
 * color, as well as to get its value and generate a List of all possible moves
 * given the board
 * @author Siddharth Vaknalli and Sean Marchand
 * @version Wednesday, January 21, 2015
 */
public class Pawn extends Piece
{
	private static final long serialVersionUID = 1L;

	// Keep track of whether this Pawn is on the
	// opposite side of the board and whether another Pawn can complete an en
	// passant move on this Pawn if legal
	protected boolean onOppositeSide;
	protected boolean enPassantPossible;

	// Keep track of the value of the Pawn in a game and a score table based on
	// how favorable each position is for each Pawn
	public int value = 100;
	private static int[][] pawnScoreTable = { { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 50, 50, 50, 50, 50, 50, 50, 50 },
			{ 10, 10, 20, 30, 30, 20, 10, 10 }, { 5, 5, 10, 27, 27, 10, 5, 5 },
			{ 0, 0, 0, 25, 25, 0, 0, 0, }, { 5, -5, -10, 0, 0, -10, -5, 5 },
			{ 5, 10, 10, -25, -25, 10, 10, 5 }, { 0, 0, 0, 0, 0, 0, 0, 0 } };

	/**
	 * Constructs a Pawn object using the given row, column and color as an
	 * integer (0 for white, 1 for black)
	 * @param row the row of this Pawn object
	 * @param column the column of this Pawn object
	 * @param color the color of this Pawn as an integer - 0 for white, and 1 as
	 *            black
	 */
	public Pawn(int row, int column, int color)
	{
		// Call the super constructor with the given row, column and appropriate
		// image
		super(row, column, color, color == 0 ? new ImageIcon("Temp\\Pawn0.png")
				.getImage() : new ImageIcon("Temp\\Pawn1.png").getImage());

		// Keep track of which side this Pawn exists
		if (row == 1)
			onOppositeSide = true;
		else
			onOppositeSide = false;

		// The pawn cannot be captured in an en
		// passant move
		enPassantPossible = false;
	}
	
	/**
	 * Gets the value of this Pawn according to its position on the board
	 * depending on its color, using its score table
	 */
	public int getValue()
	{
		if (this.color == 0)
		{
			return value + pawnScoreTable[this.row][this.column];
		}
		return value + pawnScoreTable[7 - this.row][this.column];
	}

	/**
	 * Generates a List of all possible Squares to which this Pawn can move in a
	 * game of Chess given the board as an array of Squares. Also adds a Square
	 * in which this Pawn can make an en passant move to capture the appropriate
	 * Pawn of the opposite color
	 * @param board an Array of Squares representing the Chess board
	 * @return an ArrayList of Squares from this Board to which this Pawn can
	 *         legally move according to the rules of the game of Chess
	 */
	@Override
	public ArrayList<Square> generateMoves(Square[][] board)
	{
		// Keep track of the row and column of this Rook
		int row = super.row;
		int column = super.column;

		// Keep track of all the valid moves which a Pawn can make
		ArrayList<Square> validMoves = new ArrayList<Square>();

		// Else if this Pawn is on the bottom side of the board, do the
		// following
		if (!onOppositeSide)
		{
			// Check if this Pawn can be moved one square ahead (or two if its
			// in its original position)
			if (row > 0 && !board[row - 1][column].containsPiece())
			{
				validMoves.add(board[row - 1][column]);
				if (!this.hasMoved)
				{
					if (!board[row - 2][column].containsPiece())
						validMoves.add(board[row - 2][column]);
				}
			}

			// Check whether any Pieces can be captured in the Square
			// diagonally above to the left
			if (row > 0 && column > 0
					&& board[row - 1][column - 1].containsPiece()
					&& !board[row - 1][column - 1].piece.isSameColor(this))
			{
				validMoves.add(board[row - 1][column - 1]);
			}

			// Check whether any Pieces can be captured in the Square
			// diagonally above to the right
			if (row > 0 && column < 7
					&& board[row - 1][column + 1].containsPiece()
					&& !board[row - 1][column + 1].piece.isSameColor(this))
			{
				validMoves.add(board[row - 1][column + 1]);
			}

			if (Board.twoPlayerScreen)
			{
				// Check whether an en passant move can be made on a Pawn in a
				// Square to the left of this one
				if (column > 0 &&
						board[row][column - 1].piece instanceof Pawn)
				{
					Pawn adjacent =
							(Pawn) board[row][column - 1].piece;
					if (!adjacent.isSameColor(this)
							&& adjacent.enPassantPossible)
						validMoves.add(board[row - 1][column - 1]);
				}

				// Check whether an en passant move can be made on a Pawn in a
				// Square to the right of this one
				if (column < 7 &&
						board[row][column + 1].piece instanceof Pawn)
				{
					Pawn adjacent =
							(Pawn) board[row][column + 1].piece;
					if (!adjacent.isSameColor(this)
							&& adjacent.enPassantPossible)
						validMoves.add(board[row - 1][column + 1]);
				}

			}

		}

		// Else if this Pawn is on the top side of the board, do the
		// following
		else if (onOppositeSide)
		{
			// Check if this Pawn can be moved one square ahead (or two if its
			// in its original position)
			if (row < 7 && !board[row + 1][column].containsPiece())
			{
				validMoves.add(board[row + 1][column]);
				if (!this.hasMoved)
				{
					if (!board[row + 2][column].containsPiece())
						validMoves.add(board[row + 2][column]);
				}
			}

			// Check whether any Pieces can be captured in the Square
			// diagonally below to the left
			if (row < 7 && column > 0)
				if (column > 0 && board[row + 1][column - 1].containsPiece()
						&& !board[row + 1][column - 1].piece.isSameColor(this))
					validMoves.add(board[row + 1][column - 1]);

			// Check whether any Pieces can be captured in the Square
			// diagonally below to the right
			if (column < 7 && row < 7
					&& board[row + 1][column + 1].containsPiece()
					&& !board[row + 1][column + 1].piece.isSameColor(this))
			{
				validMoves.add(board[row + 1][column + 1]);
			}

			if (Board.twoPlayerScreen)
			{
				// Check whether an en passant move can be made on a Pawn in a
				// Square to the left of this one
				if (column > 0 && row < 7 &&
						board[row][column - 1].piece instanceof Pawn)
				{
					Pawn adjacent =
							(Pawn) board[row][column - 1].piece;
					if (!adjacent.isSameColor(this)
							&& adjacent.enPassantPossible)
					{
						validMoves.add(board[row + 1][column - 1]);
					}
				}

				// Check whether an en passant move can be made on a Pawn in a
				// Square to the right of this one
				if (column < 7 &&
						board[row][column + 1].piece instanceof Pawn)
				{
					Pawn adjacent =
							(Pawn) board[row][column + 1].piece;
					if (!adjacent.isSameColor(this)
							&& adjacent.enPassantPossible)
					{
						validMoves.add(board[row + 1][column + 1]);
					}
				}

			}

		}

		// Return the list of valid legal moves of this Pawn
		return validMoves;
	}

}
