import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * Keeps track of a Queen class for a Chess game, and keeps track of its value
 * and a score table for its position on the board. Inherits data and methods
 * from the Piece class. Includes methods to construct a Queen given the row,
 * column and color, as well as to get its value and to generate a List of all
 * possible moves given the board
 * 
 * @author Siddharth Vaknalli and Sean Marchand
 * @version Wednesday, January 21, 2015
 */
public class Queen extends Piece
{

	private static final long serialVersionUID = 1L;

	// Keep track of the value of the Queen and a score table to represent its
	// score on the board according to its position
	public int value = 1000;
	private static int[][] queenScoreTable = {
			{ -40, -20, -20, -20, -20, -20, -20, -40 },
			{ 50, 50, 50, 50, 50, 50, 50, 50 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 50, 50, 50, 50, 50, 50, 50, 50 },
			{ 50, 50, 50, 50, 50, 50, 50, 50 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { -40, 0, 0, 0, 0, 0, 0, -40 } };

	/**
	 * Constructs a Queen given the row, columm and color
	 * @param row the row of the Queen
	 * @param column the column of the Queen
	 * @param color the color of the Queen
	 */
	public Queen(int row, int column, int color)
	{
		super(row, column, color,
				color == 0 ? new ImageIcon("Temp\\Queen0.png").getImage()
						: new ImageIcon("Temp\\Queen1.png").getImage());
	}
	
	/**
	 * Gets the value of this Queen according to its position on the board
	 * depending on its color, using its score table
	 */
	public int getValue()
	{
		if (this.color == 0)
		{
			return value + queenScoreTable[this.row][this.column];
		}
		return value + queenScoreTable[7 - this.row][this.column];
	}

	/**
	 * Generates a List of all possible Squares to which this Queen can move in
	 * a game of Chess given the board as an array of Squares
	 * @param board an Array of Squares representing the Chess board
	 * @return an ArrayList of Squares from this Board to which this Queen can
	 *         legally move according to the rules of the game of Chess
	 */
	@Override
	public ArrayList<Square> generateMoves(Square[][] board)
	{
		// Keep track of the row and column of this Queen
		int row = super.row;
		int column = super.column;

		// Keep track of the ArrayList of Squares to which this Rook can legally
		// move in a game of Chess
		ArrayList<Square> validMoves = new ArrayList<Square>();

		// Keep track of whether a Piece exists in the direction being checked
		boolean pieceInCheckDirection = false;

		// Keep track of the Square being checked in any direction
		Square check;

		// Check the Squares above the current position of this Queen on the
		// given board until a Piece in its path is found
		for (int checkRow = row - 1; checkRow >= 0 && !pieceInCheckDirection; checkRow--)
		{
			// Keep track of the Square being checked
			check = board[checkRow][column];

			// If the Square being checked does not contain a Piece or contains
			// a Piece that is a different color than this Queen, highlight the
			// Square and add it to the ArrayList
			if (!check.containsPiece() || !check.piece.isSameColor(this))
			{
				validMoves.add(check);

				// If the Square contains a Piece, it cannot be moved beyond
				// that Square
				if (check.containsPiece())
					pieceInCheckDirection = true;
			}

			// If the Square contains a Piece of the same color as this Queen,
			// it cannot be moved to that or beyond that Square
			else
				pieceInCheckDirection = true;
		}

		// Check the Squares below the current position of this Queen on the
		// given board until a Piece in its path is found
		pieceInCheckDirection = false;
		for (int checkRow = row + 1; checkRow < board.length
				&& !pieceInCheckDirection; checkRow++)
		{
			// Keep track of the Square being checked in any direction
			check = board[checkRow][column];

			// If the Square being checked does not contain a Piece or contains
			// a Piece that is a different color than this Queen, highlight the
			// Square and add it to the ArrayList
			if (!check.containsPiece() || !check.piece.isSameColor(this))
			{
				validMoves.add(check);

				// If the Square contains a Piece, it cannot be moved beyond
				// that Square
				if (check.containsPiece())
					pieceInCheckDirection = true;
			}

			// If the Square contains a Piece of the same color as this Queen,
			// it cannot be moved to that or beyond that Square
			else
				pieceInCheckDirection = true;
		}

		// Check the Squares to the left of the current position of this Queen
		// of the given board until a Piece in its path is found
		pieceInCheckDirection = false;
		for (int checkCol = column - 1; checkCol >= 0 && !pieceInCheckDirection; checkCol--)
		{
			// Keep track of the Square being checked in any direction
			check = board[row][checkCol];

			// If the Square being checked does not contain a Piece or contains
			// a Piece that is a different color than this Queen, highlight the
			// Square and add it to the ArrayList
			if (!check.containsPiece() || !check.piece.isSameColor(this))
			{
				validMoves.add(check);

				// If the Square contains a Piece, it cannot be moved beyond
				// that Square
				if (check.containsPiece())
					pieceInCheckDirection = true;
			}

			// If the Square contains a Piece of the same color as this Queen,
			// it cannot be moved to that or beyond that Square
			else
				pieceInCheckDirection = true;
		}

		// Check the Squares to the right of the current position of this Queen
		// on the given board until a Piece in its path is found
		pieceInCheckDirection = false;
		for (int checkCol = column + 1; checkCol < board[row].length
				&& !pieceInCheckDirection; checkCol++)
		{
			// Keep track of the Square being checked in any direction
			check = board[row][checkCol];

			// If the Square being checked does not contain a Piece or contains
			// a Piece that is a different color than this Queen, highlight the
			// Square and add it to the ArrayList
			if (!check.containsPiece() || !check.piece.isSameColor(this))
			{
				validMoves.add(check);

				// If the Square contains a Piece, it cannot be moved beyond
				// that Square
				if (check.containsPiece())
					pieceInCheckDirection = true;
			}

			// If the Square contains a Piece of the same color as this Queen,
			// it cannot be moved to that or beyond that Square
			else
				pieceInCheckDirection = true;
		}

		// Check each Square diagonally above to the right of the Square of this
		// Queen to check for valid moves
		pieceInCheckDirection = false;
		for (int checkRow = row - 1, checkCol = column + 1; checkRow >= 0
				&& checkCol < board[row].length && !pieceInCheckDirection; checkRow--, checkCol++)
		{
			// Keep track of the Square being checked
			check = board[checkRow][checkCol];

			// If the Square being checked does not contain a Piece or contains
			// a Piece that is a different color than this Queen, highlight the
			// Square and add it to the ArrayList
			if (!check.containsPiece() || !check.piece.isSameColor(this))
			{
				validMoves.add(check);

				// If the Square contains a Piece, it cannot be moved beyond
				// that Square
				if (check.containsPiece())
					pieceInCheckDirection = true;
			}

			// If the Square contains a Piece of the same color as this Queen,
			// it cannot be moved to that or beyond that Square
			else
				pieceInCheckDirection = true;
		}

		// Check each Square diagonally above to the left of the Square of this
		// Queen to check for valid moves
		pieceInCheckDirection = false;
		for (int checkRow = row - 1, checkCol = column - 1; checkRow >= 0
				&& checkCol >= 0 && !pieceInCheckDirection; checkRow--, checkCol--)
		{
			// Keep track of the Square being checked
			check = board[checkRow][checkCol];

			// If the Square being checked does not contain a Piece or contains
			// a Piece that is a different color than this Queen, highlight the
			// Square and add it to the ArrayList
			if (!check.containsPiece() || !check.piece.isSameColor(this))
			{
				validMoves.add(check);

				// If the Square contains a Piece, it cannot be moved beyond
				// that Square
				if (check.containsPiece())
					pieceInCheckDirection = true;
			}

			// If the Square contains a Piece of the same color as this Queen,
			// it cannot be moved to that or beyond that Square
			else
				pieceInCheckDirection = true;
		}

		// Check each Square diagonally below to the left of the Square of this
		// Queen to check for valid moves
		pieceInCheckDirection = false;
		for (int checkRow = row + 1, checkCol = column - 1; checkRow < board.length
				&& checkCol >= 0 && !pieceInCheckDirection; checkRow++, checkCol--)
		{
			// Keep track of the Square being checked
			check = board[checkRow][checkCol];

			// If the Square being checked does not contain a Piece or contains
			// a Piece that is a different color than this Queen, highlight the
			// Square and add it to the ArrayList
			if (!check.containsPiece() || !check.piece.isSameColor(this))
			{
				validMoves.add(check);

				// If the Square contains a Piece, it cannot be moved beyond
				// that Square
				if (check.containsPiece())
					pieceInCheckDirection = true;
			}

			// If the Square contains a Piece of the same color as this Queen,
			// it cannot be moved to that or beyond that Square
			else
				pieceInCheckDirection = true;
		}

		// Check each Square diagonally below to the right of the Square of this
		// Queen to check for valid moves
		pieceInCheckDirection = false;
		for (int checkRow = row + 1, checkCol = column + 1; checkRow < board.length
				&& checkCol < board[row].length && !pieceInCheckDirection; checkRow++, checkCol++)
		{
			// Keep track of the Square being checked
			check = board[checkRow][checkCol];

			// If the Square being checked does not contain a Piece or contains
			// a Piece that is a different color than this Queen, highlight the
			// Square and add it to the ArrayList
			if (!check.containsPiece() || !check.piece.isSameColor(this))
			{
				validMoves.add(check);

				// If the Square contains a Piece, it cannot be moved beyond
				// that Square
				if (check.containsPiece())
					pieceInCheckDirection = true;
			}

			// If the Square contains a Piece of the same color as this Queen,
			// it cannot be moved to that or beyond that Square
			else
				pieceInCheckDirection = true;
		}

		// Return all the valid Squares to which this Queen can move
		return validMoves;
	}

}
